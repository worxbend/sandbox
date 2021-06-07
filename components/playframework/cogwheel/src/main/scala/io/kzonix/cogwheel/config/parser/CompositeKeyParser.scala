package io.kzonix.cogwheel.config.parser

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigList
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueFactory
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.PathWithSequenceIndex
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.ParamNamePatters.SequenceIndex
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toConfigKey
import io.kzonix.cogwheel.config.parser.CompositeKeyParser.Folder

import scala.jdk.CollectionConverters._
import scala.annotation.tailrec

class CompositeKeyParser(nextParser: ParameterParser[Map[String, String], ConfigValue])
    extends ParameterParser[Map[String, String], ConfigValue] {

  private def buildConfigTree(parameters: Map[String, String]): Map[Folder, ConfigValue] = {
    val interimMapStruct: Map[Folder, ConfigValue] = parameters
      .groupMap { case (path, _) => Folder(path)() } { case (_, value) => ValueParser.parseStringValue(value) }
      .map { case (agg, value) => agg -> ConfigValueFactory.fromIterable(value.asJava) }
    buildConfigTree(
      interimMapStruct,
      Map.empty
    )
  }

  @tailrec private final def buildConfigTree(denormalizedConfigTree: Map[Folder, ConfigValue],
                                             accumulator: Map[Folder, ConfigValue]): Map[Folder, ConfigValue] = {
    // group last-level nodes by nearest common parent param-name part
    val interimTree                          = denormalizedConfigTree
      .groupMap {
        case (agg, _) =>
          val pathParts       = agg.paramName.split("/").filter(_.nonEmpty)
          val index           = pathParts.reverse.takeWhile(!_.matches(SequenceIndex)).length + 1
          val slicedPathParts = pathParts.dropRight(index + 1)
          val droppedPath     = pathParts.drop(pathParts.length - 1 - index)
          val configKey       = droppedPath.filter(!_.matches(SequenceIndex))
          if (slicedPathParts.nonEmpty)
            Folder(slicedPathParts.mkString("/"))(toConfigKey(configKey.mkString("/")))
          else
            Folder(agg.paramName)(toConfigKey(configKey.mkString("/")))
      } {
        case (_, value) =>
          value
      }
    // merging config entry with appropriate upper-level key by constructing new config-object value
    //  config { "enclosed-key" : prev config-object }
    val foldedTree: Map[Folder, ConfigValue] = interimTree.map {
      case (agg, value) =>
        val configObject: ConfigValue = value.foldLeft(ConfigValueFactory.fromIterable(Seq.empty.asJava)) {
          case (acc, list: ConfigList)  => ConfigValueFactory.fromIterable((acc.asScala ++ list.asScala).asJava)
          case (acc, list: ConfigValue) => ConfigValueFactory.fromIterable((acc.asScala ++ List(list)).asJava)
        }
        Option(agg.configKey)
          .filter(_.nonEmpty)
          .map((cfgKey: String) => agg -> configObject.atPath(cfgKey).root())
          .getOrElse(agg -> configObject)
    }
    // find already built tree
    val normalizedInterimTree                = foldedTree.filter {
      case (agg, _) => PathWithSequenceIndex.r.findFirstIn(agg.paramName).isEmpty
    }
    // reducing amount of data for further recursive invocation in case if current structure parsing (param name) is exhausted
    val nextTrees                            = foldedTree -- normalizedInterimTree.keySet
    if (nextTrees.nonEmpty)
      buildConfigTree(
        nextTrees,
        normalizedInterimTree ++ accumulator
      )
    else normalizedInterimTree ++ accumulator

  }

  def parse(parameters: Map[String, String]): ConfigObject = {
    val (compositeParams, simpleParams)      = parameters.partition {
      case (path, _) => PathWithSequenceIndex.r.findAllIn(path).nonEmpty
    }
    val configTree: Map[Folder, ConfigValue] = buildConfigTree(compositeParams)
    // extract map of all root level config trees
    val rootConfigObjects                    = configTree.map { case (agg, config) => config.atPath(toConfigKey(agg.paramName)).root() }
    // combine root level objects into single one:
    //  if map of config-object contains more than one entry, then it means that root config object consists of different keys, e.g:
    //   { "root1": { some chile config tree }, "root2": { some chile config tree }
    //  and such elements should be merged into single one
    rootConfigObjects
      .foldLeft(ConfigFactory.empty().root()) {
        case (c1, c2) => c1.withFallback(c2)
      }
      .withFallback(nextParser.parse(simpleParams))
  }

}

object CompositeKeyParser {
  private case class Folder(paramName: String)(var configKey: String = "")

  object ParamNamePatters {
    val SequenceIndex         = "[0-9]+"
    val PathWithSequenceIndex = s"/$SequenceIndex(/)?"

  }

}
