package io.kzonix.index.routes

import io.kzonix.index.controllers.IndexController
import io.kzonix.sird.{ProvidedRouter, RoutePrefix}
import io.kzonix.sird.RouteVersioningHelper.RoutePrefixWithVersion
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class IndexRoute @Inject()(controller: IndexController) extends SimpleRouter with ProvidedRouter {

  import play.api.mvc.Handler

  override val routePrefix: RoutePrefix = "/main".withVersion(1)

  val b: Map[RequestMethodExtractor, Handler] = Map[RequestMethodExtractor, Handler]()
  override def routes: Routes = {
    case GET(p"/index")      => controller.index
    case GET(p"/asyncCache") => controller.asyncCacheTest
    case GET(p"/syncCache")  => controller.cacheTest
  }
}
