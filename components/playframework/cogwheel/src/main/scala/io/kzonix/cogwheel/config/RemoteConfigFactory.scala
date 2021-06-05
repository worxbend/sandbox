package io.kzonix.cogwheel.config

import com.typesafe.config._
import io.kzonix.cogwheel.aws.AWSParameterStoreClient
import io.kzonix.cogwheel.config.ConfigPathUtils.PathUtils._

class RemoteConfigFactory(
    parameterStoreClient: AWSParameterStoreClient
) {

  def loadConfig(namespace: String, configKey: String): Config = {
    val paramPath                   = toParamPath(configKey).withConfigNameSpace(namespace)
    val params: Map[String, String] = parameterStoreClient.fetchParameters(paramPath)
    ConfigParser.parseParameters(params)
  }

}

object RemoteConfigFactory {}
