package io.kzonix.play

import io.kzonix.sird.SirdProvider
import play.api.ApplicationLoader
import play.api.inject.bind
import play.api.inject.guice.{GuiceApplicationLoader, GuiceableModule}
import play.api.routing.Router

/**
 * An ApplicationLoader that uses Guice to bootstrap the application.
 * It bind [[Router]] to [[SirdProvider]].
 */
class SimpleApplicationLoader extends GuiceApplicationLoader {

  override protected def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] =
    super.overrides(context) :+ (bind[Router].toProvider[SirdProvider]: GuiceableModule)

}
