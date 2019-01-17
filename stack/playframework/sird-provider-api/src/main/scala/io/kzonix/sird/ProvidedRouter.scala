package io.kzonix.sird

import play.api.routing.Router

trait ProvidedRouter extends Router {

  def routePrefix: String = ""

  def routeVersion: Int = 0

  // TODO: change to lazy val
  private def routeVersion(prefix: String)(ver: Int): String = Router.concatPrefix("/v" + ver, prefix)

  private lazy val versioned = routeVersion(routePrefix)(_)

  final lazy val prefix: String = {
    if (routeVersion == 0) {
      routePrefix
    } else {
      versioned(routeVersion)
    }
  }
}
