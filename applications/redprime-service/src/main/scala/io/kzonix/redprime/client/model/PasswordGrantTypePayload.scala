/*
 * Copyright (c) 2020 Kzonix Projects
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
