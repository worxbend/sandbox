package io.kzonix.index.filters

import play.api.Configuration
import play.api.mvc.EssentialAction
import play.api.mvc.EssentialFilter
import play.api.mvc.RequestHeader
import play.api.mvc.Result

import javax.inject._
import scala.concurrent.ExecutionContext

/**
 * This is a simple filter that adds a header to all requests. It's
 * added to the application's list of filters by the
 * [[Filters]] class.
 *
 * @param ec This class is needed to execute code asynchronously.
 *           It is used below by the `map` method.
 */
@Singleton
class DeploymentEnvIdFilter @Inject() (config: Configuration)(implicit ec: ExecutionContext) extends EssentialFilter {

  override def apply(next: EssentialAction): EssentialAction =
    EssentialAction { request: RequestHeader =>
      val value: Option[EnvId] = config.getOptional[EnvId]("docker.env")
      next(request).map { result: Result =>
        result.withHeaders("X-Container-Id" -> value.map((_: EnvId).id).getOrElse(""))
      }
    }

}
