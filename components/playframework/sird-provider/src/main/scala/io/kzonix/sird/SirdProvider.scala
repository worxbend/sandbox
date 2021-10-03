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

import play.api.http.HttpConfiguration
import play.api.routing.Router

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides a fully-constructed, composed instance of [[Router]].
 *
 * @constructor create a new instance of route provides with routes collection and http configuration.
 * @param routes     Collection of all defined [[ProvidedRouter]] across whole scope of DI-container.
 * @param httpConfig Play http configuration contains context path value which is needed to construct and combine routers.
 */
@Singleton
class SirdProvider @Inject() (routes: Set[ProvidedRouter], httpConfig: HttpConfiguration) extends RouterProvider {

  /**
   * Provides a fully-constructed and injected instance of [[Router]].
   *
   * @throws RuntimeException if the injector encounters an error while
   *                          providing an instance. For example, if an injectable member on
   *                          [[Router]] an exception, the injector may wrap the exception
   *                          and throw it to the caller of [[get()]]. Callers should not try
   *                          to handle such exceptions as the behavior may vary across injector
   *                          implementations and even different configurations of the same injector.
   */
  override def get(): Router =
    routes
      .map { (router: ProvidedRouter) =>
        router.withPrefix(
          Router.concatPrefix(
            httpConfig.context,
            router.prefix
          )
        )
      }
      .reduce((current: Router, next: Router) => current.orElse(next))

}
