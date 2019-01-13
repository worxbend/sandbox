package io.kzonix.slib.controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SpecialController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with Endpoints {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(page: Option[Int]) = Action { implicit request: Request[AnyContent] =>
    val thePage = page.getOrElse(1)
    Results.Ok(s"Items page $thePage")
    Ok(s"Not items page $thePage")
  }
}