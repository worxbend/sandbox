package io.kzonix.sird

import play.api.routing.Router

/** Uses for definition String Interpolation Route Definition */
trait ProvidedRouter extends Router {

  /**
   * Provides a router prefix based on router configuration: base prefix and route version.
   *
   * @return String prefix
   */
  final lazy val prefix: String = {
    if (routeVersion == 0) routePrefix else versioned(routeVersion)
  }
  
  private lazy val versioned = routeWithVersion(routePrefix)(_: Int)

  /**
   * Route path. Can be overrode in implementation of [[Router]].
   */
  abstract def routePrefix: String = ""

  /**
   * Route version. Can be overrode in implementation of [[Router]].
   */
  abstract def routeVersion: Int = 0
}
