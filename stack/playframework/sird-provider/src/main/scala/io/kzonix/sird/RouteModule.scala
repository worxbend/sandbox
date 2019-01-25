package io.kzonix.sird

import play.api.inject.Binding
import play.api.{Configuration, Environment}

class RouteModule extends play.api.inject.Module {
  def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[RouterProvider].to[SirdProvider])
  }
}