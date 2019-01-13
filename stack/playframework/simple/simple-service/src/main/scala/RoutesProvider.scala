import io.kzonix.slib.KzonixRouter
import javax.inject.{ Inject, Provider }
import play.api.http.HttpConfiguration
import play.api.routing.Router

class RoutesProvider @Inject() (appRouter: AppRouter, strings: Set[KzonixRouter], httpConfig: HttpConfiguration) extends Provider[Router] {

  override def get(): Router = {
    strings
      .map(rp => rp.get().withPrefix(httpConfig.context))
      .reduce((r1, r2) => r1.orElse(r2))
  }
}
