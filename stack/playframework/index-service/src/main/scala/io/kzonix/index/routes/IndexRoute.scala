package io.kzonix.index.routes

import io.kzonix.index.controllers.IndexController
import io.kzonix.sird.ProvidedRouter
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class IndexRoute @Inject()(controller: IndexController) extends SimpleRouter with ProvidedRouter {

  import play.api.mvc.Handler

  override def routePrefix: String = "/main/"

  override def routeVersion: Int = 1

  val b = Map[RequestMethodExtractor, Handler]()
  override def routes: Routes = {
    case GET(p"/index") => controller.index
  }
}
