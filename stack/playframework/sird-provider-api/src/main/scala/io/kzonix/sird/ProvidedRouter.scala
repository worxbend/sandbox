package io.kzonix.sird

import play.api.routing.Router

trait ProvidedRouter extends Router {
  def routePrefix: String = ""

  def ver: Int = 0

  private def routeVersion(prefix: String)(ver: Int) : String = "/v" + ver + "/" + prefix

  private def versioned = routeVersion(routePrefix) _

  def withVersion : String  = {
    if (ver == 0) {
      routePrefix
    } else {
      versioned(ver)
    }
  }
}
