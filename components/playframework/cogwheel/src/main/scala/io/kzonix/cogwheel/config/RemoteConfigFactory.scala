package io.kzonix.cogwheel.config

import com.typesafe.config._
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
    // TODO: validation
    val parsedParams                = params.map { case (path, value) => path -> ValueParser.parseStringValue(value) }
    new CompositeKeyParser(new SimpleKeyParser()).parse(parsedParams).toConfig
  }

}

object RemoteConfigFactory {}
