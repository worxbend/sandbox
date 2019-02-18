package io.kzonix.sird

import play.api.{ Configuration, Environment }
import play.api.inject.Binding

class RouteModule extends play.api.inject.Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] =
    Seq(bind[RouterProvider].to[SirdProvider])
}
