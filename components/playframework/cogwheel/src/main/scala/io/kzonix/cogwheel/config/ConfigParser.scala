package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toConfigKey

import scala.annotation.tailrec
import scala.collection.immutable
import scala.jdk.CollectionConverters._
import scala.util.Try

object ConfigParser {

  private def parseValue(value: String): ConfigValue = {
    val triedObject                       = parseJsonString(value)
    val configObject: Option[ConfigValue] = triedObject.toOption
    configObject.getOrElse(ConfigValueFactory.fromAnyRef(value))
  }

  private def parseJsonString(value: String): Try[ConfigObject] = {
    val parsedConfig = Try(ConfigFactory.parseString(value))
    parsedConfig.map { cfg =>
       val configMap = cfg
         .root()
         .keySet()
         .asScala
         .map(key => key -> cfg.getAnyRef(key))
         .toMap
       ConfigValueFactory.fromMap(configMap.asJava)
    }

  }

  val ParameterSequence = "/seq_[0-9]+(/)?".r

  def parseParameters(params: Map[String, String]): Config = {

    val filteredParams = params.filter { case (path, _) => ParameterSequence.findAllIn(path).nonEmpty }

    val list = compositeConfigSequence(filteredParams)

    val remainingSimpleParams = params.removedAll(filteredParams.keySet)

    val configObjects: Map[String, ConfigMergeable] = remainingSimpleParams.map {
      case (path, value) => toConfigKey(path) -> parseValue(value)
    }

    val configObject = ConfigFactory.parseMap(configObjects.asJava).root().withFallback(list)
    configObject.toConfig
  }

  def buildConfigTree(filteredParams: Map[String, String]) = {
    val interimMapStruct: Map[TempAgg, ConfigValue] = filteredParams
      .groupMap {
        case (path, _) =>
          TempAgg(path)()
      } {
        case (_, value) =>
          parseValue(value)
      }
      .map {
        case (agg, value) =>
          agg -> ConfigValueFactory.fromIterable(value.asJava)
      }
    buildConfigTree_(
      interimMapStruct,
      Map.empty
    )
  }

  @tailrec def buildConfigTree_(tree: Map[TempAgg, ConfigValue],
                                acc: Map[TempAgg, ConfigValue]): Map[TempAgg, ConfigValue] = {
    val grooped                             = tree
      .groupMap {
        case (agg, _) =>
          val pathParts       = agg.path.split("/").filter(_.nonEmpty)
          val index           = pathParts.reverse.takeWhile(!_.startsWith("seq_")).length
          val slicedPathParts = pathParts.dropRight(index + 1)
          val dropedPath      = pathParts.drop(pathParts.length - 1 - index)
          val configKey       = dropedPath.filter(!_.startsWith("seq_"))
          val flatLast        = dropedPath.length != configKey.length
          if (slicedPathParts.nonEmpty)
            TempAgg(slicedPathParts.mkString("/"))(
              toConfigKey(
                (configKey
                  .mkString("/"))
                  .split(ParameterSequence.regex)
                  .mkString("/")
              ),
              flatLast
            )
          else
            TempAgg(agg.path)(
              toConfigKey(
                (configKey
                  .mkString("/"))
                  .split(ParameterSequence.regex)
                  .mkString("/")
              ),
              flatLast
            )
      } {
        case (_, value) =>
          value
      }
    val newTrees: Map[TempAgg, ConfigValue] = grooped
      .map {
        case (agg, value) =>
          val configObject: ConfigValue = value.foldLeft(ConfigValueFactory.fromIterable(Seq.empty.asJava)) {
            case (acc, list: ConfigList)  =>
              ConfigValueFactory.fromIterable((acc.asScala ++ list.asScala).asJava)
            case (acc, list: ConfigValue) =>
              ConfigValueFactory.fromIterable((acc.asScala ++ List(list)).asJava)
          }
          if (agg.configKey.nonEmpty)
            agg -> configObject.atPath(agg.configKey).root()
          else
            agg -> configObject

      }

    val builtTrees = newTrees.filter { case (agg, _) => ParameterSequence.findFirstIn(agg.path).isEmpty }

    val nextTrees = newTrees -- builtTrees.keySet
    if (nextTrees.nonEmpty)
      buildConfigTree_(
        nextTrees,
        builtTrees ++ acc
      )
    else builtTrees ++ acc

  }

  case class TempAgg(path: String)(var configKey: String = "", var boolean: Boolean = false)
  case class Node(path: String)

  private def compositeConfigSequence(filteredParams: Map[String, String]) = {

    val value1: Map[TempAgg, ConfigValue] = buildConfigTree(filteredParams)
    println(value1)

    val interimMapStruct: Map[Seq[String], ConfigMergeable] = filteredParams
      .groupMap {
        case (path, _) =>
          val pathParts = ParameterSequence.split(
            path
          )
          pathParts.toSeq.reverse
      } {
        case (_, value) =>
          parseValue(value)
      }
      .map {
        case (path, iter) =>
          path -> ConfigValueFactory.fromIterable(iter.asJava)
      }

    val compositeConfigTree: Map[Seq[String], ConfigMergeable] = composite(interimMapStruct)
    val flatten: immutable.Iterable[ConfigMergeable]           = compositeConfigTree.flatMap { case (_, iter) => Seq(iter) }
    val configMergeable: ConfigMergeable                       = flatten.foldLeft(ConfigValueFactory.fromMap(Map.empty[String, Any].asJava)) {
      case (c1: ConfigMergeable, c2: ConfigMergeable) => c1.withFallback(c2)
    }
    configMergeable
  }

  @tailrec def composite(interimMapStruct: Map[Seq[String], ConfigMergeable]): Map[Seq[String], ConfigMergeable] = {

    val array: Map[Seq[String], ConfigMergeable] = interimMapStruct
      .groupMap {
        case (pathParts, _) =>
          if (pathParts.isEmpty) pathParts else pathParts.tail
      } {
        case (pathParts, value: ConfigMergeable) =>
          if (pathParts.isEmpty) value
          else {
            val java = Map(toConfigKey(pathParts.head) -> value).asJava
            ConfigFactory.parseMap(java).root()
          }
      }
      .map {
        case (path, iter) =>
          path -> iter.foldLeft(ConfigValueFactory.fromMap(Map.empty[String, Any].asJava)) {
            case (c1: ConfigMergeable, c2: ConfigMergeable) => c1.withFallback(c2)
          }
      }
    if (array.keySet.flatten.isEmpty)
      array
    else
      composite(array)

  }

}
