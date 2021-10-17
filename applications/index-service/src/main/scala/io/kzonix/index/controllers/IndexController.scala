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

package io.kzonix.index.controllers

import play.api.cache.AsyncCacheApi
import play.api.cache.SyncCacheApi
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

@Singleton
class IndexController @Inject() (
    cc: ControllerComponents,
    cache: AsyncCacheApi,
    syncCache: SyncCacheApi
)(implicit
    ec: ExecutionContext
) extends AbstractController(cc) {

  def index: Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      Future {
        println(request)
        Ok(Json.toJson("Hello world"))
      }
    }

  // Test the NPE issue #9476 with caffeine async api
  def asyncCacheTest: Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      Future {
        println(request)
        val futureValue: Future[String] = cache.getOrElseUpdate[String]("item.key") {
          Future.successful(null)
        }
        val value: String               = Await.result(
          futureValue,
          2.seconds
        )
        Ok(Json.toJson(s"Hello $value"))
      }
    }

  // Test the NPE issue #9476 with caffeine sync api
  def cacheTest: Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      Future {
        println(request)
        val value: String = syncCache.getOrElseUpdate[String]("item.key") {
          null
        }
        Ok(Json.toJson(s"Hello $value"))
      }
    }

}
