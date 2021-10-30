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

package io.kzonix.redprime.actors

import akka.actor.Actor
import akka.actor.Props
import io.kzonix.redprime.actors.RedditUserOverviewActor.Tick
import io.kzonix.redprime.client.RedditClient
import io.kzonix.redprime.client.model.OAuthResponse
import play.api.Logger

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object RedditUserOverviewActor {

  def props: Props = Props[RedditUserOverviewActor]()

  case object Tick
}

class RedditUserOverviewActor @Inject() (
    val rc: RedditClient
)(implicit ec: ExecutionContext)
    extends Actor {

  private val logger: Logger = Logger(this.getClass)

  override def receive: Receive = {
    case Tick =>
      rc.login().map { (maybeRes: Option[OAuthResponse]) =>
        maybeRes match {
          case Some(res) => logger.info(res.expiresIn.toString)
          case None      => logger.info("Could not obtain access to the account.")
        }
      }
  }

}
