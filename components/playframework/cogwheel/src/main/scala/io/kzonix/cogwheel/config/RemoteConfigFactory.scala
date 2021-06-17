package io.kzonix.cogwheel.config

import com.typesafe.config._
import com.typesafe.scalalogging.LazyLogging
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils._
import io.kzonix.cogwheel.config.parser.CompositeKeyParser
import io.kzonix.cogwheel.config.parser.SimpleKeyParser
import io.kzonix.cogwheel.config.parser.ValueParser
import org.checkerframework.checker.units.qual.s

import scala.util.Failure
import scala.util.Try

class RemoteConfigFactory(
    parameterStoreClient: AWSParameterStoreClient
) extends LazyLogging {

  def loadConfig(namespace: String, configKey: String): Config = {
    val paramPath                   = toParamName(configKey).withConfigNameSpace(namespace)
    val rootConfigKey               = toConfigKey(paramPath)
    logger.info(s"Loading config by key $rootConfigKey")
    val params: Map[String, String] = parameterStoreClient.fetchParameters(paramPath)
    logger.info(s"Loaded ${ params.size } key-value pair(s)")
    val parsedParams                = params.map { case (path, value) => path -> ValueParser.parseStringValue(value) }
    val successParams               = validateOrThrow(parsedParams)

    parseConfig(successParams).getObject(rootConfigKey).toConfig
  }

  private def parseConfig(params: Map[String, ConfigValue]) = {
    logger.info(s"Parsing ${ params.size } param(s)...")
    logger.whenDebugEnabled {
      logger.debug(params.toString)
    }

    new CompositeKeyParser(new SimpleKeyParser())
      .parse(params)
      .toConfig
  }

  private def validateOrThrow(parsedParams: Map[String, Try[ConfigValue]]): Map[String, ConfigValue] = {
    val failedParams = parsedParams.filter { case (_, parsedRes) => parsedRes.isFailure }
    if (failedParams.nonEmpty) {
      val downFields = failedParams.map {
        case (path, Failure(exception)) =>
          s
          logger.error(
            s"Failed entry with key '${ toConfigKey(path) }':",
            exception
          )
          DownField(toConfigKey(path))
      }
      throw DecodingFailure(
        s"Failed to parse configuration at key: ",
        downFields.toList
      )
    } else parsedParams.map { case (path, success) => path -> success.get }

  }

}

object RemoteConfigFactory {}
