package controllers

import forms.{CreateUserForm, LoginUserForm, UserIDForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.UserService

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
      "Postleitzahl" -> number,
      "Stadt" -> text,
      "Rolle" -> text
    )(CreateUserForm.apply)(CreateUserForm.unapply))
  val loginForm = Form(
    mapping(
      "Nachname" -> text,
      "Postleitzahl" -> number
    )(LoginUserForm.apply)(LoginUserForm.unapply))

  val deleteUserForm = Form {
    mapping(
      "Kunden-ID" -> longNumber
    )(UserIDForm.apply)(UserIDForm.unapply)
  }

  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */
  def addUser: Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      userData => {
        models.calculateDeliveryTime(userData.zipcode, userData.name)
        if (models.DeliveryTime.expectedTime == -1) {
          Redirect(routes.UserController.attemptFailed("register"))
        } else {
          services.UserService.addUser(userData.forename, userData.name, userData.address, userData.zipcode, userData.city, userData.role)
          Redirect(routes.UserController.welcomeUser())
        }
      })
  }

  def deleteUser: Action[AnyContent] = Action { implicit request =>
    deleteUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editUser(null, formWithErrors))
      },
      userData => {
        services.UserService.deleteUser(userData.customerID)
        Redirect(routes.UserController.editUser())
      })
  }

  /**
    * Shows the welcome view.
    */
  def welcomeUser: Action[AnyContent] = Action {
    if (models.activeUser.role.equals("Mitarbeiter")) {
      Redirect(routes.UserController.welcomeEmployee())
    } else {
      Ok(views.html.welcomeUser())
    }
  }

  def welcomeEmployee: Action[AnyContent] = Action {
    Ok(views.html.welcomeEmployee(services.UserService.registeredUsers, controllers.OrderHistoryController.userOrdersForm))
  }

  def attemptFailed(errorcode: String): Action[AnyContent] = Action {
    Ok(views.html.attemptFailed(errorcode))
  }

  def attemptSuccessful: Action[AnyContent] = Action {
    Ok(views.html.attemptSuccessful())
  }

  /**
    * List all users currently available in the system.
    */
  def showUsers: Action[AnyContent] = Action {
    Ok(views.html.showUsers(UserService.registeredUsers))
  }

  def loginUser: Action[AnyContent] = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      },
      loginData => {
        val loggedinUser = services.UserService.loginUser(loginData.name, loginData.zipcode)
        if (loggedinUser == -1) {
          Redirect(routes.UserController.attemptFailed("login"))
        } else {
          Redirect(routes.UserController.welcomeUser())
        }
      })
  }

  def logoutUser: Action[AnyContent] = Action {
    services.UserService.logoutUser()
    Redirect(routes.Application.index())
  }

  def editUser: Action[AnyContent] = Action {
    Ok(views.html.editUser(controllers.UserController.userForm, controllers.UserController.deleteUserForm))
  }
}
