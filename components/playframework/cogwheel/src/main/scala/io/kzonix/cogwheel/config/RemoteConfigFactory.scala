package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils._
import io.kzonix.cogwheel.config.parser.CompositeKeyParser
import io.kzonix.cogwheel.config.parser.SimpleKeyParser

class RemoteConfigFactory(
    parameterStoreClient: AWSParameterStoreClient
) {

  def loadConfig(namespace: String, configKey: String): Config = {
    val paramPath                   = toParamName(configKey).withConfigNameSpace(namespace)
    val params: Map[String, String] = parameterStoreClient.fetchParameters(paramPath)

    new CompositeKeyParser(new SimpleKeyParser())
      .parse(params)
      .toConfig
  }

}

object RemoteConfigFactory {}
