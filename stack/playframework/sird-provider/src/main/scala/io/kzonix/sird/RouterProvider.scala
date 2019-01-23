package io.kzonix.sird

import javax.inject.Provider
import play.api.routing.Router

/**
  * Provides a fully-constructed, composed instance of [[Router]].
  **/
trait RouterProvider extends Provider[Router]
