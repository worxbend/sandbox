package io.kzonix.slib

import io.kzonix.slib.controllers.SpecialController2
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class MyAppRouter2 @Inject() (controller: SpecialController2) extends SimpleRouter with KzonixRouter {

  override def routes: Routes = {
    case GET(p"/maybeitems" ? q_o"page=${ int(page) }") => controller.index(page)
  }
}
