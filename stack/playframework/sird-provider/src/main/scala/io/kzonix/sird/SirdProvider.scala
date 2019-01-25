package io.kzonix.sird

import javax.inject.{Inject, Singleton}
import play.api.http.HttpConfiguration
import play.api.routing.Router

/**
  * Provides a fully-constructed, composed instance of [[Router]].
  *
  * @constructor create a new instance of route provides with routes collection and http configuration.
  * @param routes     Collection of all defined [[ProvidedRouter]] across whole scope of DI-container.
  * @param httpConfig Play http configuration contains context path value which is needed to construct and combine routers.
  */
@Singleton
class SirdProvider @Inject()(routes: Set[ProvidedRouter], httpConfig: HttpConfiguration) extends RouterProvider {

  /**
    * Provides a fully-constructed and injected instance of [[Router]].
    *
    * @throws RuntimeException if the injector encounters an error while
    *                          providing an instance. For example, if an injectable member on
    *                          [[Router]] an exception, the injector may wrap the exception
    *                          and throw it to the caller of [[get()]]. Callers should not try
    *                          to handle such exceptions as the behavior may vary across injector
    *                          implementations and even different configurations of the same injector.
    */
  override def get(): Router = routes
    .map(router => {
      Console.println(router)
      router.withPrefix(Router.concatPrefix(httpConfig.context, router.prefix))
    })
    .reduce((current, next) => current.orElse(next))
}
