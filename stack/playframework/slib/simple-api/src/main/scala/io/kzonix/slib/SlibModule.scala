package io.kzonix.slib

import com.google.inject.AbstractModule
import io.kzonix.slib.controllers.SpecialController
import net.codingwell.scalaguice.{ ScalaModule, ScalaMultibinder }

class SlibModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val kzonixRouter = ScalaMultibinder.newSetBinder[KzonixRouter](binder)
    kzonixRouter.addBinding.to[SlibKzonixRouter]
  }
}
