package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.{Form, Forms}
import play.api.data.Forms.{mapping, text}
import services.UserService
import forms.{CreateUserForm, LoginUserForm}

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object UserController extends Controller {

  /**
   * Form object for user data.
   */
  val userForm = Form(
    mapping(
      "Vorname" -> text,
      "Name" -> text,
      "Straße und Hausnummer" -> text,
      "Postleitzahl" -> Forms.number,
      "Stadt" -> text,
      "Rolle" -> text
    )(CreateUserForm.apply)(CreateUserForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addUser : Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.index(formWithErrors))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.forename, userData.name, userData.address, userData.zipcode, userData.city, userData.role)
        Redirect(routes.UserController.welcomeUser(newUser.forename, newUser.name)).
          flashing("success" -> "User saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */
  def welcomeUser(forename: String, name: String) : Action[AnyContent] = Action {
    Ok(views.html.welcomeUser(forename, name))
  }

  /**
   * List all users currently available in the system.
   */
  def showUsers : Action[AnyContent] = Action {
    Ok(views.html.users(UserService.registeredUsers))
  }

  val loginForm = Form(
    mapping(
      "Nachname" -> text,
      "Postleitzahl" -> Forms.number
    )(LoginUserForm.apply)(LoginUserForm.unapply))

  def loginUser : Action[AnyContent] = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      },
      loginData => {
        val logginginUser = services.UserService.loginUser(loginData.name, loginData.zipcode)
        if (logginginUser != -1) {
          Redirect(routes.UserController.welcomeUser(logginginUser.toString, loginData.name))
        } else {
          Redirect(routes.UserController.welcomeUser("Login", "Fehlgeschlagen"))
        }
      })
  }
}
