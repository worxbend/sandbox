package io.kzonix.index.routes

import com.google.inject.AbstractModule
import io.kzonix.sird.ProvidedRouter
import net.codingwell.scalaguice.ScalaModule
import net.codingwell.scalaguice.ScalaMultibinder

class RouteModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    val routeMultiBinder: ScalaMultibinder[ProvidedRouter] =
      ScalaMultibinder.newSetBinder[ProvidedRouter](binder)
    routeMultiBinder.addBinding.to[IndexRoute]
  }

}
