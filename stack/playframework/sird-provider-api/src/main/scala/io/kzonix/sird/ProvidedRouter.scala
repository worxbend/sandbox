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
  private lazy val versioned = routeWithVersion(routePrefix)(_)

  /**
   * Route path. Can be overrode in implementation of [[Router]].
   */
  def routePrefix: String = ""

  /**
   * Route version. Can be overrode in implementation of [[Router]].
   */
  def routeVersion: Int = 0
}
