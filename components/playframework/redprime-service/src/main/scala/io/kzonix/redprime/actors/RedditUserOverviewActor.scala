package io.kzonix.redprime.actors

import akka.actor.Actor
import akka.actor.Props
import io.kzonix.redprime.actors.RedditUserOverviewActor.Tick
import io.kzonix.redprime.client.RedditClient
import io.kzonix.redprime.client.model.OAuthResponse
import play.api.Logger

import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

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
      val promise: Future[Unit] = rc.login.map { (maybeRes: Option[OAuthResponse]) =>
        maybeRes match {
          case Some(res) => logger.info(res.expiresIn.toString)
        }
      }
      Await.result(
        promise,
        20.seconds
      )

  }

}
