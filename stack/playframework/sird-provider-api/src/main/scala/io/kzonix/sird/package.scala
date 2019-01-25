package io.kzonix

import play.api.routing.Router

/** Provides classes for dealing with play SIRD mechanism. */
package object sird {

  /**
    * Concat route version with base route path.
    *
    * ==Overview==
    * The first parameter is route prefix. It can be a string like `/parent/child/` or `parent/child`.
    * In the result you will get something like `/v1/parent/child`.
    *
    * @param prefix Route base prefix
    * @param ver    Route version
    * @return Concatenated string with route version and path.
    */
  private[sird] lazy val routeWithVersion: String => Int => String = (prefix: String) => (ver: Int) => Router.concatPrefix("/v" + ver, prefix)
}