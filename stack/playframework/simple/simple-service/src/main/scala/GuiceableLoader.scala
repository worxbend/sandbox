import play.api.ApplicationLoader
import play.api.inject.guice.{ GuiceApplicationLoader, GuiceableModule }
import play.api.routing.Router
import play.api.inject._

class GuiceableLoader extends GuiceApplicationLoader {

  protected override def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] = {
    super.overrides(context) :+ (bind[Router].toProvider[RoutesProvider]: GuiceableModule)
  }

}
