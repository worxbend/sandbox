/*
 * Copyright (c) 2020 Kzonix Projects
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.kzonix.sird

import io.kzonix.sird.ProvidedRouter.routeWithVersion
import play.api.routing.Router

/** Uses for definition String Interpolation Route Definition */
trait ProvidedRouter extends Router {

  /** Provides a router prefix based on router configuration: base prefix and route version.
    *
    * @return
    *   String prefix
    */
  final lazy val prefix: String = routeWithVersion(routePrefix)
  val routePrefix: RoutePrefix
}

private[sird] object ProvidedRouter {

  /** Concat route version with the base route path.
    *
    * 122Overview122 The first parameter is route prefix. It can be a string like `/parent/child/` or `parent/child`. In
    * the result you will get something like `/v1/parent/child`.
    *
    * @return
    *   Function responsible to concat route version and path.
    */
  private[sird] def routeWithVersion: RoutePrefix => String = (routePrefix: RoutePrefix) =>
    if (routePrefix.isVersional)
      Router.concatPrefix(
        "v".concat(routePrefix.version.toString),
        routePrefix.prefix
      )
    else
      routePrefix.prefix

}
