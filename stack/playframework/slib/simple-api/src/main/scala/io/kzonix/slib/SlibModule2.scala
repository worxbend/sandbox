package io.kzonix.slib

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.{ ScalaModule, ScalaMultibinder }

class SlibModule2 extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val kzonixRouter = ScalaMultibinder.newSetBinder[KzonixRouter](binder)
    kzonixRouter.addBinding.to[MyAppRouter2]
  }
}
