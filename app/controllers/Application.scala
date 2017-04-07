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
  def index: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("user").isDefined) {
      Console.println("Value of 'user' from Session logged in: " + request2session.get("user"))
      Console.println("Logged in?" + models.activeUser.loggedin)
      Redirect(routes.UserController.welcomeUser())
    } else {
      Console.println("Value of 'user' from Session not logged in: " + request2session.get("user"))
      Console.println("Logged in?" + models.activeUser.loggedin)
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