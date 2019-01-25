package io.kzonix.index.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IndexController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index: Action[JsValue] = Action(parse.json).async {
    implicit request => {
      Future(Ok(s"Hello world $request"))
    }
  }
}
