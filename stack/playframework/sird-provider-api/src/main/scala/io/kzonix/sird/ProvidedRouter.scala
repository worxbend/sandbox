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
  def routePrefix: String = ""

  /**
   * Route version. Can be overrode in implementation of [[Router]].
   */
  def routeVersion: Int = 0

  /**
   * Concat route version with the base route path.
   *
   * ==Overview==
   * The first parameter is route prefix. It can be a string like `/parent/child/` or `parent/child`.
   * In the result you will get something like `/v1/parent/child`.
   *
   * @return Function responsible to concat route version and path.
   */
  private[sird] lazy val routeWithVersion: String => Int => String = (prefix: String) =>
    (ver: Int) => Router.concatPrefix("/v" + ver, prefix)
}
