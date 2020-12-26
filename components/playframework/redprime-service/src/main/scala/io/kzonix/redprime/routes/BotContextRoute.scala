package io.kzonix.redprime.routes

import io.kzonix.redprime.controllers.BotContextController
import io.kzonix.sird.RouteVersioningHelper.RoutePrefixWithVersion
import io.kzonix.sird.ProvidedRouter
import io.kzonix.sird.RoutePrefix
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird.GET
import play.api.routing.sird.UrlContext

import javax.inject.Inject

class BotContextRoute @Inject() (controller: BotContextController) extends SimpleRouter with ProvidedRouter {

  override val routePrefix: RoutePrefix = "/reddit-bot".withVersion(1)

  override def routes: Routes = {
    case GET(p"/index/${number}") => controller.index(number)
    case GET(p"/asyncCache")      => controller.asyncCacheTest
    case GET(p"/syncCache")       => controller.cacheTest
  }

}
