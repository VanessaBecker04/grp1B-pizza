package controllers

import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Main controller of the Pizza Service application.
  *
  * @author ob, scs, Maximilian Öttl
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

  /**
    * Shows the register page of the application.
    *
    * @return register web page
    */
  def register: Action[AnyContent] = Action {
    Ok(views.html.register(controllers.UserController.userForm))
  }

  /**
    * Shows the login page of the application.
    *
    * @return login web page
    */
  def login: Action[AnyContent] = Action {
    Ok(views.html.login(controllers.UserController.loginForm))
  }

  /**
    * Shows the contact page of the application.
    *
    * @return contact web page
    */
  def contact: Action[AnyContent] = Action {
    Ok(views.html.contact())
  }
}