package io.kzonix.index.filters

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.server.Server

import scala.concurrent.ExecutionContext

/**
 * This is a simple filter that adds a header to all requests. It's
 * added to the application's list of filters by the
 * [[Filters]] class.
 *
 * @param ec This class is needed to execute code asynchronously.
 * It is used below by the `map` method.
 */
@Singleton
class DeploymentEnvIdFilter @Inject()(config: Configuration)(implicit ec: ExecutionContext) extends EssentialFilter {

  override def apply(next: EssentialAction): EssentialAction =
    EssentialAction { request: RequestHeader =>
      {
        val value = config.get[EnvId]("docker.env")
        next(request).map { result: Result =>
          result.withHeaders("X-Container-Id" -> value.id)
        }
      }
    }
}
