package io.kzonix.sird

import io.kzonix.sird.ProvidedRouter.routeWithVersion
import play.api.routing.Router

/** Uses for definition String Interpolation Route Definition */
trait ProvidedRouter extends Router {

  /**
   * Provides a router prefix based on router configuration: base prefix and route version.
   *
   * @return String prefix
   */
  final lazy val prefix: String = routeWithVersion(routePrefix)
  val routePrefix: RoutePrefix
}

private[sird] object ProvidedRouter {

  /**
   * Concat route version with the base route path.
   *
   * ==Overview==
   * The first parameter is route prefix. It can be a string like `/parent/child/` or `parent/child`.
   * In the result you will get something like `/v1/parent/child`.
   *
   * @return Function responsible to concat route version and path.
   */
  private[sird] def routeWithVersion: RoutePrefix => String = (routePrefix: RoutePrefix) => {
    if (routePrefix.isVersional)
      Router.concatPrefix(
        "v".concat(routePrefix.version.toString),
        routePrefix.prefix
      )
    else
      routePrefix.prefix
  }

}
