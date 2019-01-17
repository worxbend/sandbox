package io.kzonix.sird

import play.api.ApplicationLoader
import play.api.inject.bind
import play.api.inject.guice.{ GuiceApplicationLoader, GuiceableModule }
import play.api.routing.Router

class SimpleApplicationLoader extends GuiceApplicationLoader {

  protected override def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] = {
    super.overrides(context) :+ (bind[Router].toProvider[SirdProvider]: GuiceableModule)
  }

}
