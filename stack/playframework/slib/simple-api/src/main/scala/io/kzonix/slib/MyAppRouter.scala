package io.kzonix.slib

import io.kzonix.slib.controllers.SpecialController
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class MyAppRouter @Inject() (controller: SpecialController) extends SimpleRouter with KzonixRouter {

  override def prefix: String = "/v2/"
  override def routes: Routes = {
    case GET(p"/notitems" ? q_o"page=${ int(page) }") => controller.index(page)
  }
}
