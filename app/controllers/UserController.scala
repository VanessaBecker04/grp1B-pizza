package controllers

import forms.{CreateUserForm, EditUserForm, LoginUserForm, UserIDForm}
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
      "Email" -> text.verifying("Bitte Ihre Email angeben", !_.isEmpty),
      "Passwort" -> text.verifying("Bitte Ihr Passwort angeben", !_.isEmpty),
      "Vorname" -> text.verifying("Bitte Ihren Vornamen angeben", !_.isEmpty),
      "Name" -> text.verifying("Bitte Ihren Namen angeben", !_.isEmpty),
      "Straße und Hausnummer" -> text.verifying("Bitte Ihre Straße und Hausnummer angeben", !_.isEmpty),
      "Postleitzahl" -> number,
      "Stadt" -> text.verifying("Bitte Ihre Stadt angeben", !_.isEmpty),
      "Rolle" -> text.verifying("Bitte Ihre Rolle angeben", !_.isEmpty)
    )(CreateUserForm.apply)(CreateUserForm.unapply))

  val loginForm = Form(
    mapping(
      "Email" -> text,
      "Passwort" -> text
    )(LoginUserForm.apply)(LoginUserForm.unapply))

  val editUserForm = Form(
    mapping(
      "Kunden-ID" -> longNumber,
      "Email" -> text.verifying("Bitte eine Email angeben", !_.isEmpty),
      "Passwort" -> text.verifying("Bitte ein Passwort angeben", !_.isEmpty),
      "Vorname" -> text.verifying("Bitte einen Vornamen angeben", !_.isEmpty),
      "Name" -> text.verifying("Bitte einen Namen angeben", !_.isEmpty),
      "Straße und Hausnummer" -> text.verifying("Bitte eine Straße und Hausnummer angeben", !_.isEmpty),
      "Postleitzahl" -> number,
      "Stadt" -> text.verifying("Bitte eine Stadt angeben", !_.isEmpty),
      "Rolle" -> text.verifying("Bitte eine Rolle angeben", !_.isEmpty)
    )(EditUserForm.apply)(EditUserForm.unapply))

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
          val user = services.UserService.addUser(userData.email, userData.password, userData.forename, userData.name, userData.address, userData.zipcode, userData.city, userData.role)
          if (user != null) {
            if (models.activeUser.role.equals("Mitarbeiter")) {
              Redirect(routes.UserController.attemptSuccessful("usercreated"))
            } else {
              Redirect(routes.UserController.welcomeUser())
            }
          } else {
            Redirect(routes.UserController.attemptFailed("emailused"))
          }
        }
      })
  }

  def editUser: Action[AnyContent] = Action { implicit request =>
    editUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editUsers(null, null, formWithErrors, null))
      },
      userData => {
        models.calculateDeliveryTime(userData.zipcode, userData.name)
        if (models.DeliveryTime.expectedTime == -1) {
          Redirect(routes.UserController.attemptFailed("register"))
        } else {
          val user = services.UserService.editUser(userData.customerID, userData.email, userData.password, userData.forename, userData.name, userData.address, userData.zipcode, userData.city, userData.role)
          if (user != null) {
            Redirect(routes.UserController.attemptSuccessful("useredited"))
          } else {
            Redirect(routes.UserController.attemptFailed("emailused"))
          }
        }
      })
  }

  def deleteUser: Action[AnyContent] = Action { implicit request =>
    deleteUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editUsers(null, null, null, formWithErrors))
      },
      userData => {
        services.UserService.deleteUser(userData.customerID)
        Redirect(routes.UserController.attemptSuccessful("userdeleted"))
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
    Ok(views.html.welcomeEmployee())
  }

  def attemptFailed(errorcode: String): Action[AnyContent] = Action {
    Ok(views.html.attemptFailed(errorcode))
  }

  def attemptSuccessful(successcode: String): Action[AnyContent] = Action {
    Ok(views.html.attemptSuccessful(successcode))
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
        val loggedinUser = services.UserService.loginUser(loginData.email, loginData.password)
        if (loggedinUser == -1) {
          Redirect(routes.UserController.attemptFailed("login"))
        } else if (loggedinUser == -2) {
          Redirect(routes.UserController.attemptFailed("inactive"))
        } else {
          Redirect(routes.UserController.welcomeUser())
        }
      })
  }

  def logoutUser: Action[AnyContent] = Action {
    services.UserService.logoutUser()
    Redirect(routes.Application.index())
  }

  def editUsers: Action[AnyContent] = Action {
    if(models.activeUser.role.equals("Mitarbeiter")) {
      Ok(views.html.editUsers(services.UserService.registeredUsers, controllers.UserController.userForm, controllers.UserController.editUserForm, controllers.UserController.deleteUserForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }
}
