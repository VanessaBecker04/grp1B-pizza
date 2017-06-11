package controllers

import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Main controller of the Pizza Service application.
  *
  * @author ob, scs, Maximilian Öttl
  */
object Application extends Controller {

  /**
    * Shows the welcomeUser view or the index view.
    *
    * @return main web page or welcome User view
    */
  def index: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("user").isDefined) {
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
  def register: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.register(controllers.UserController.userForm))
  }

  /**
    * Shows the login page of the application.
    *
    * @return login web page
    */
  def login: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login(controllers.UserController.loginForm))
  }

  /**
    * Shows the contact page of the application.
    *
    * @return contact web page
    */
  def contact: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.contact())
  }
}