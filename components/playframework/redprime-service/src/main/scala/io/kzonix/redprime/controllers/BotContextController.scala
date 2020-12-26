package io.kzonix.redprime.controllers

import play.api.cache.AsyncCacheApi
import play.api.cache.SyncCacheApi
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.Inject
import javax.inject.Singleton
import scala.concurrent.duration.DurationInt
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

@Singleton
class BotContextController @Inject() (
    cc: ControllerComponents,
    cache: AsyncCacheApi,
    syncCache: SyncCacheApi
)(implicit
    ec: ExecutionContext
) extends AbstractController(cc) {

  def index(num: String): Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      Future {
        Ok(Json.toJson("Hello world and " + num))
      }
    }

  // Test the NPE issue #9476 with caffeine async api
  def asyncCacheTest: Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      Future {
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
        val value: String = syncCache.getOrElseUpdate[String]("item.key") {
          null
        }
        Ok(Json.toJson(s"Hello $value"))
      }
    }

}
