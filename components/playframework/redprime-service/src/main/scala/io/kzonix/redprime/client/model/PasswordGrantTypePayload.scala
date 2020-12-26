package io.kzonix.redprime.client.model

import com.typesafe.config.Config
import play.api.ConfigLoader
import play.api.Logger

case class PasswordGrantTypePayload(
    authUri: String,
    clientId: String,
    clientSecret: String,
    userName: String,
    password: String,
    grantType: String = "password"
)

object PasswordGrantTypePayload {

  private val logger: Logger = Logger(this.getClass)

  implicit val configLoader: ConfigLoader[PasswordGrantTypePayload] = (config: Config, path: String) => {
    val rootConfig: Config = config.getConfig(path)
    val payload            = PasswordGrantTypePayload(
      authUri = rootConfig.getString("authorizeUri"),
      clientId = rootConfig.getString("clientId"),
      clientSecret = rootConfig.getString("clientSecret"),
      userName = rootConfig.getString("username"),
      password = rootConfig.getString("password")
    )
    logger.info(payload.toString)
    payload
  }

}
