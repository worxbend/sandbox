package io.kzonix.redprime.actors

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ActorConfigurationModule extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit =
    bindActor[RedditUserOverviewActor]("RedditUserOverviewActor")

}
