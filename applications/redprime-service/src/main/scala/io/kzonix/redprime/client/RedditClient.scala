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

package io.kzonix.redprime.client

import akka.http.scaladsl.model.HttpMethods.POST
import com.google.inject.Inject
import io.kzonix.redprime.client.RedditClient.LoginQueryParams._
import io.kzonix.redprime.client.model.OAuthResponse.responseReads
import io.kzonix.redprime.client.model.OAuthResponse
import io.kzonix.redprime.client.model.PasswordGrantTypePayload
import play.api.Configuration
import play.api.Logger
import play.api.libs.ws.WSAuthScheme
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSResponse

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class RedditClient @Inject() (
    ws: WSClient,
    config: Configuration
)(implicit
    val ec: ExecutionContext
) {

  private val logger: Logger = Logger(this.getClass)

  def login: Future[Option[OAuthResponse]] = {

    val cfg                                  = config.get[PasswordGrantTypePayload](path = "reddit.client")
    val eventualResponse: Future[WSResponse] = ws
      .url(cfg.authUri)
      .withAuth(
        cfg.clientId,
        cfg.clientSecret,
        WSAuthScheme.BASIC
      )
      .withQueryStringParameters(
        GrantType -> cfg.grantType,
        UserName  -> cfg.userName,
        Password  -> cfg.password
      )
      .execute(POST.value)

    eventualResponse.map { response =>
      logger.info(response.json.toString())
      response.json.validate[OAuthResponse].asOpt
    }
  }

}

object RedditClient {

  object LoginQueryParams {
    val GrantType = "grant_type"
    val UserName  = "username"
    val Password  = "password"
  }

}
