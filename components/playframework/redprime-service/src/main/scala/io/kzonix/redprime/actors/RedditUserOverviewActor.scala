package io.kzonix.redprime.actors

import akka.actor.Actor
import akka.actor.Props
import io.kzonix.redprime.actors.RedditUserOverviewActor.Tick
import io.kzonix.redprime.client.RedditClient
import io.kzonix.redprime.client.model.OAuthResponse
import play.api.Logger

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.ExecutionContext

object RedditUserOverviewActor {

  def props: Props = Props[RedditUserOverviewActor]

  case object Tick
}

class RedditUserOverviewActor @Inject() (
    val rc: RedditClient
)(implicit ec: ExecutionContext)
    extends Actor {

  private val logger: Logger = Logger(this.getClass)

  override def receive: Receive = {
    case Tick =>
      rc.login.map { (res: Option[OAuthResponse]) =>
        logger.info(s"Result 🥰 - ${LocalDateTime.now().toString}")
        logger.info(s"$res")

        sender() ! "Done"
      }
  }

}
