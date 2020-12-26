package io.kzonix.redprime.client.model

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.Json
import play.api.libs.json.JsonConfiguration
import play.api.libs.json.OFormat

case class OAuthResponse(
    accessToken: String,
    tokenType: String,
    expiresIn: Long,
    scope: String
)

object OAuthResponse {

  implicit val cfg: JsonConfiguration                = JsonConfiguration(SnakeCase)
  implicit val responseReads: OFormat[OAuthResponse] = Json.format[OAuthResponse]

}
