package io.kzonix.index.controllers

import javax.inject.{ Inject, Singleton }
import play.api.mvc.{ AbstractController, Action, AnyContent, ControllerComponents }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class IndexController @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action.async {
    implicit request =>
      {
        Future(Ok(s"Hello world $request"))
      }
  }
}
