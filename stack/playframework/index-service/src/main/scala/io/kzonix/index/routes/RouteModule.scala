package io.kzonix.index.routes

import com.google.inject.AbstractModule
import io.kzonix.sird.ProvidedRouter
import net.codingwell.scalaguice.{ ScalaModule, ScalaMultibinder }

class RouteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val stringMulti = ScalaMultibinder.newSetBinder[ProvidedRouter](binder)
    stringMulti.addBinding.to[IndexRoute]
  }
}
