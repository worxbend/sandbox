package io.kzonix.redprime.tasks

import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.google.inject.Inject
import com.google.inject.name.Named
import io.kzonix.redprime.actors.RedditUserOverviewActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class RedditUserOverviewTask @Inject() (
    actorSystem: ActorSystem,
    @Named("RedditUserOverviewActor") actorRef: ActorRef
)(implicit
    executionContext: ExecutionContext
) {
  actorSystem.scheduler.scheduleAtFixedRate(
    initialDelay = 0.microseconds,
    interval = 60.seconds,
    receiver = actorRef,
    message = RedditUserOverviewActor.Tick
  )
}
