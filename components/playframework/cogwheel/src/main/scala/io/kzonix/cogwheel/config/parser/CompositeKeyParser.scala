package io.kzonix.cogwheel.config.parser

import com.typesafe.config._
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.PathWithSequenceIndex
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.SequenceIndex

import scala.jdk.CollectionConverters._

class CompositeKeyParser(nextParser: ParameterParser[Map[String, String], ConfigValue])
    extends ParameterParser[Map[String, String], ConfigValue] {

  private def buildTree(node: Node): ConfigValue = {

    val value1: Seq[Node] = node.children
      .filter { case (key, _) => key.nonEmpty }
      .groupMap {
        case (key, _) =>
          key.split("/").filter(_.nonEmpty).head
      } {
        case (key, value) =>
          key.split("/").filter(_.nonEmpty).tail.mkString("/") -> value

      }
      .map {
        case (key, value) =>
          if (key.matches(SequenceIndex))
            node.indexed()
          Node(
            key,
            value.toMap
          )()
      }
      .toSeq

    if (value1.isEmpty) {
      val str = node.children.values.toSeq(0)
      ValueParser.parseStringValue(str)
    } else {
      val value: Seq[ConfigValue] = value1.map { node =>
        if (!node.id.matches(SequenceIndex)) {
          val configObject = buildTree(node).atPath(node.id).root()
          configObject
        } else {
          val configObject = buildTree(node)
          configObject
        }
      }
      if (node.isIndexed) ConfigValueFactory.fromIterable(value.asJava)
      else {
        val configObject = value.foldLeft(ConfigFactory.empty().root()) { case (c1, c2) => c1.withFallback(c2) }
        configObject
      }
    }
  }

  case class Node(id: String, children: Map[String, String])(var isIndexed: Boolean = false) {
    def indexed(): Unit = this.isIndexed = true
  }

  def parse(parameters: Map[String, String]): ConfigObject = {
    val (compositeParams, simpleParams) = parameters.partition {
      case (path, _) => PathWithSequenceIndex.r.findAllIn(path).nonEmpty
    }
    val cfg                             = if (compositeParams.nonEmpty) {
      val config = buildTree(
        Node(
          "root",
          compositeParams
        )()
      )
      config.atKey("root").getConfig("root")
    } else ConfigFactory.empty()

    cfg.root().withFallback(nextParser.parse(simpleParams))
  }

}

object CompositeKeyParser {
  private case class Folder(paramName: String)(var configKey: String = "")

  object ParamNamePatters {
    val SequenceIndex         = "[0-9]+"
    val PathWithSequenceIndex = s"/$SequenceIndex(/)?"

  }

}
