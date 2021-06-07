package io.kzonix.cogwheel.config.parser

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigMergeable
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValue
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils.toConfigKey

import scala.jdk.CollectionConverters._

class SimpleKeyParser extends ParameterParser[Map[String, String], ConfigValue] {

  def parse(parameters: Map[String, String]): ConfigObject = {
    val configObjects: Map[String, ConfigMergeable] = parameters.map {
      case (path, value) => toConfigKey(path) -> ValueParser.parseStringValue(value)
    }

    ConfigFactory.parseMap(configObjects.asJava).root()
  }

}
