import io.kzonix.slib.KzonixRouter
import javax.inject.{ Inject, Provider }
import play.api.http.HttpConfiguration
import play.api.routing.Router

class RoutesProvider @Inject() (appRouter: AppRouter, routers: Set[KzonixRouter], httpConfig: HttpConfiguration) extends Provider[Router] {

  override def get(): Router = {
    routers
      .map(rp => {
        rp.withPrefix(Router.concatPrefix(httpConfig.context, rp.prefix))
      })
      .reduce((r1, r2) => r1.orElse(r2))
      .orElse(appRouter)
  }
}
