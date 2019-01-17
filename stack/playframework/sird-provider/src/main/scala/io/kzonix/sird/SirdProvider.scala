package io.kzonix.sird

import javax.inject.Inject
import play.api.http.HttpConfiguration
import play.api.routing.Router

class SirdProvider @Inject() (routes: Set[ProvidedRouter], httpConfig: HttpConfiguration) extends RouterProvider {

  override def get(): Router = {
    routes
      .map(router => {
        router.withPrefix(Router.concatPrefix(httpConfig.context, router.prefix))
      })
      .reduce((current, next) => current.orElse(next))
  }
}
