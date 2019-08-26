package io.kzonix.index.controllers

import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class IndexController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def index: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      Ok(Json.toJson("Hello world"))
    }
  }
}
