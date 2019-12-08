package io.kzonix.index.controllers

import javax.inject.{ Inject, Singleton }
import play.api.cache.{ AsyncCacheApi, SyncCacheApi }
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContext, Future }

@Singleton
class IndexController @Inject()(cc: ControllerComponents, cache: AsyncCacheApi, syncCache: SyncCacheApi)(
    implicit ec: ExecutionContext
) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      Ok(Json.toJson("Hello world"))
    }
  }

  // Test the NPE issue #9476 with caffeine async api
  def asyncCacheTest: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val futureValue = cache.getOrElseUpdate[String]("item.key") {
        Future.successful(null)
      }
      val value = Await.result(futureValue, 2.seconds)
      Ok(Json.toJson(s"Hello $value"))
    }
  }

  // Test the NPE issue #9476 with caffeine sync api
  def cacheTest: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val value = syncCache.getOrElseUpdate[String]("item.key") {
        null
      }
      Ok(Json.toJson(s"Hello $value"))
    }
  }
}
