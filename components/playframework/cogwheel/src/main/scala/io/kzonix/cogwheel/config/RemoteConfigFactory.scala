package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils._
import io.kzonix.cogwheel.config.parser.CompositeKeyParser
import io.kzonix.cogwheel.config.parser.SimpleKeyParser
import io.kzonix.cogwheel.config.parser.ValueParser

class RemoteConfigFactory(
    parameterStoreClient: AWSParameterStoreClient
) {

  def loadConfig(namespace: String, configKey: String): Config = {
    val paramPath                   = toParamName(configKey).withConfigNameSpace(namespace)
    val params: Map[String, String] = parameterStoreClient.fetchParameters(paramPath)
    val parsedParams                = params.map { case (path, value) => path -> ValueParser.parseStringValue(value) }
    val failedParams                = parsedParams.filter { case (_, triedConfigValue) => triedConfigValue.isFailure }
    if (failedParams.nonEmpty) {
      val downFields = failedParams.map {
        case (path, _) =>
          // log error for more details even if typesafe config provides bad explanation of error.
          DownField(path)
      }
      throw DecodingFailure(
        s"Failed to parse configuration at key: ",
        downFields.toList
      )
    }
    new CompositeKeyParser(new SimpleKeyParser())
      .parse(parsedParams.map { case (path, success) => path -> success.get })
      .toConfig
  }

}

object RemoteConfigFactory {}
