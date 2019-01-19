package io.kzonix

import play.api.routing.Router

package object sird {

  private[sird] lazy val routeWithVersion: String => Int => String = (prefix: String) => (ver: Int) => Router.concatPrefix("/v" + ver, prefix)
}
