package io.kzonix.sird

import play.api.routing.Router

/** Provides */
trait ProvidedRouter extends Router {

  /**
    * Route path. Can be overrode in implementation of [[Router]].
    * */
  def routePrefix: String = ""

  /**
    * Route version. Can be overrode in implementation of [[Router]].
    * */
  def routeVersion: Int = 0

  private lazy val versioned = routeWithVersion(routePrefix)(_)

  /**
    * Provides a router prefix based on router configuration: base prefix and route version.
    *
    * @return String prefix
    */
  final lazy val prefix: String = {
    if (routeVersion == 0) routePrefix else versioned(routeVersion)
  }
}
