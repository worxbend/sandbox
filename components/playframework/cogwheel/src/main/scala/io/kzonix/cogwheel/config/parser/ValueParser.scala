package io.kzonix.cogwheel.config.parser

import com.typesafe.config._
import io.kzonix.cogwheel.config.parser.ValueParser.ValuePatterns.JsonStructLike

import scala.util.matching.Regex

object ValueParser {

  def parseStringValue(value: String): ConfigValue =
    value match {
      case JsonStructLike(s) => parseJsonString(s)
      case _                 => ConfigValueFactory.fromAnyRef(value)
    }

  private def parseJsonString(value: String): ConfigObject = {
    val parsedConfig = ConfigFactory.parseString(value)
    parsedConfig.root()
  }

  object ValuePatterns {
    val JsonStructLike: Regex =
      raw"((?>\{\s*.*\s*\})|(?>\[\s*.*\s*\]))".r
  }

}
