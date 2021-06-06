package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toConfigKey

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._
import scala.util.Try

// TODO: Refactor (design patterns):
//  Split parsing logic into different classes according to parsing strategy and use them as chain of responsibility
//  Define it as Parsing Pipeline (take inspiration from PlayFramework - ActionBuilder or
//  https://worace.works/2021/03/07/better-play-framework-request-pipelines-with-cats/
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

  // todo: define it in some Util class to easily handle contract of parameters (path)
  val ParameterSequence = "/seq_[0-9]+(/)?".r

  def parseParameters(params: Map[String, String]): Config = {

    val filteredParams = params.filter { case (path, _) => ParameterSequence.findAllIn(path).nonEmpty }

    // TODO: rename this fucking shit
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

  // TODO:: Refactor this part with more meaningful naming
  //  - provide comment describing algorithm
  //  - experimental stuff, should be tested in couple of rounds
  @tailrec def buildConfigTree_(tree: Map[TempAgg, ConfigValue],
                                acc: Map[TempAgg, ConfigValue]): Map[TempAgg, ConfigValue] = {
    val grooped                             = tree
      .groupMap {
        case (agg, _) =>
          val pathParts       = agg.path.split("/").filter(_.nonEmpty)
          val index           = pathParts.reverse.takeWhile(!_.startsWith("seq_")).length
          val slicedPathParts = pathParts.dropRight(index + 2)
          val dropedPath      = pathParts.drop(pathParts.length - 1 - (index + 1))
          val configKey       = dropedPath.filter(!_.startsWith("seq_"))
          if (slicedPathParts.nonEmpty)
            TempAgg(slicedPathParts.mkString("/"))(
              toConfigKey(
                (configKey
                  .mkString("/"))
                  .split(ParameterSequence.regex)
                  .mkString("/")
              )
            )
          else
            TempAgg(agg.path)(
              toConfigKey(
                (configKey
                  .mkString("/"))
                  .split(ParameterSequence.regex)
                  .mkString("/")
              )
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

  case class TempAgg(path: String)(var configKey: String = "")
  case class Node(path: String)

  private def compositeConfigSequence(filteredParams: Map[String, String]) = {

    val interimConfigTree: Map[TempAgg, ConfigValue] = buildConfigTree(filteredParams)

    val rootConfigObjects  = interimConfigTree.map { case (agg, config) => config.atPath(toConfigKey(agg.path)).root() }
    val mergedConfigObject = rootConfigObjects.foldLeft(ConfigFactory.empty().root()) {
      case (c1, c2) => c1.withFallback(c2)
    }

    mergedConfigObject
  }

}
