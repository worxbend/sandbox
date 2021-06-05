package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toConfigKey

import scala.annotation.tailrec
import scala.collection.immutable
import scala.jdk.CollectionConverters._
import scala.util.Try

object ConfigParser {

  private def parseValue(value: String): ConfigMergeable = {
    val triedObject                           = parseJsonString(value)
    val configObject: Option[ConfigMergeable] = triedObject.toOption
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

  val ArrayRx = "/seq_[0-9]+(/)?".r

  def parseParameters(params: Map[String, String]): Config = {

    val filteredParams = params.filter { case (path, _) => ArrayRx.findAllIn(path).nonEmpty }

    val list = compositeConfigSequence(filteredParams)

    val remainingSimpleParams = params.removedAll(filteredParams.keySet)

    val configObjects: Map[String, ConfigMergeable] = remainingSimpleParams.map {
      case (path, value) => toConfigKey(path) -> parseValue(value)
    }

    val configObject = ConfigValueFactory.fromMap(configObjects.asJava).withFallback(list)
    configObject.toConfig
  }

  private def compositeConfigSequence(filteredParams: Map[String, String]) = {
    val interimMapStruct: Map[Seq[String], ConfigMergeable] = filteredParams
      .groupMap {
        case (path, _) =>
          val pathParts = ArrayRx.split(
            path
          )
          pathParts.toSeq
      } {
        case (_, value) =>
          parseValue(value)
      }
      .map {
        case (path, iter) =>
          path -> iter.foldLeft(ConfigValueFactory.fromMap(Map.empty[String, Any].asJava)) {
            case (c1: ConfigMergeable, c2: ConfigMergeable) => c1.withFallback(c2)
          }
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
          pathParts.dropRight(1)
      } {
        case (pathParts, value: ConfigMergeable) =>
          if (pathParts.isEmpty) value
          else {
            val java = Map(toConfigKey(pathParts.last) -> value).asJava
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
