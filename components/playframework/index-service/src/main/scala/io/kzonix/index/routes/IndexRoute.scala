package io.kzonix.index.routes

import io.kzonix.index.controllers.IndexController
import io.kzonix.sird.ProvidedRouter
import io.kzonix.sird.RoutePrefix
import io.kzonix.sird.RouteVersioningHelper.RoutePrefixWithVersion
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird.GET
import play.api.routing.sird.UrlContext

class IndexRoute @Inject() (controller: IndexController) extends SimpleRouter with ProvidedRouter {

  override val routePrefix: RoutePrefix = "/main".withVersion(1)

  override def routes: Routes = {
    case GET(p"/index")      => controller.index
    case GET(p"/asyncCache") => controller.asyncCacheTest
    case GET(p"/syncCache")  => controller.cacheTest
  }

}
