package io.kzonix.sird

import play.api.routing.Router

trait ProvidedRouter extends Router {

  def routePrefix: String = ""

  def routeVersion: Int = 0

  private lazy val versioned = routeWithVersion(routePrefix)(_)

  final lazy val prefix: String = {
    if (routeVersion == 0) {
      routePrefix
    } else {
      versioned(routeVersion)
    }
  }
}
