package io.kzonix.sird

import play.api.routing.Router

import javax.inject.Provider

/**
 * Provides a fully-constructed, composed instance of [[Router]].
 */
trait RouterProvider extends Provider[Router]
