package io.kzonix.slib

import play.api.routing.Router

trait KzonixRouter extends Router {

  def prefix: String = ""

}