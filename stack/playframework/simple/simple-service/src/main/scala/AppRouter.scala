import controllers.HomeController
import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class AppRouter @Inject() (controller: HomeController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/items" ? q_o"page=${ int(page) }") => controller.index(page)
  }
}
