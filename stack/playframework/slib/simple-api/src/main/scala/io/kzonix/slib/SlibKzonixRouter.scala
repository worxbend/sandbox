package io.kzonix.slib
import javax.inject.Inject
import play.api.routing.Router

class SlibKzonixRouter @Inject() (myAppRouter: MyAppRouter) extends KzonixRouter {
  override def get(): Router = myAppRouter
}
