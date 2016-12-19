package controllers

import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Main controller of the Pizza Service application.
  *
  * @author ob, scs
  */
object Application extends Controller {

  /**
    * Shows the start page of the application.
    *
    * @return main web page
    */
  def index: Action[AnyContent] = Action {
    if (models.activeUser.id != 0) {
      Redirect(routes.UserController.welcomeUser())
    } else {
      Ok(views.html.index())
    }
  }

  def register: Action[AnyContent] = Action {
    Ok(views.html.register(controllers.UserController.userForm))
  }

  def login: Action[AnyContent] = Action {
    Ok(views.html.login(controllers.UserController.loginForm))
  }
}