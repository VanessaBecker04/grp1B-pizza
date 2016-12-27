package controllers

import forms.{CreateMenuForm, CreateRemoveFromMenuForm, CreateUpdateInMenuForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

/**
  * Created by Hasi on 21.11.2016.
  */
object MenuController extends Controller {

  /**
    * Form object for user data.
    */
  val menuForm = Form(
    mapping(
      "Produktname" -> text, "Preis je Einheit" -> of[Double], "Kategorie" -> text)(CreateMenuForm.apply)(CreateMenuForm.unapply))
  val rmForm = Form(mapping("Id" -> longNumber)(CreateRemoveFromMenuForm.apply)(CreateRemoveFromMenuForm.unapply))
  val updateForm = Form(mapping("Id" -> longNumber, "Neuer Name" -> text, "Neuer Preis" -> of[Double])
  (CreateUpdateInMenuForm.apply)(CreateUpdateInMenuForm.unapply))

  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */

  def addToMenu: Action[AnyContent] = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(formWithErrors, null, null))
      },
      userData => {
        val newProduct = services.MenuService.addToMenu(userData.name, userData.price, userData.category)
        Redirect(routes.MenuController.editMenu())
      })
  }

  def updateInMenu: Action[AnyContent] = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, null, formWithErrors))
      },
      userData => {
        services.MenuService.updateInMenu(userData.id, userData.name, userData.price)
        Redirect(routes.MenuController.editMenu())
      })
  }

  def rmFromMenu: Action[AnyContent] = Action { implicit request =>
    rmForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, formWithErrors, null))
      },
      userData => {
        for (k <- services.MenuService.addedToMenu) {
          if (k.id == userData.id && !k.ordered) {
            services.MenuService.rmFromMenu(userData.id)
          } else {
            Redirect(routes.MenuController.editMenu())
          }
        }
        Redirect(routes.MenuController.editMenu())
      })
  }


  def editMenu: Action[AnyContent] = Action {
    if(models.activeUser.role.equals("Mitarbeiter")) {
      models.putAllMenuIDInList()
      Ok(views.html.editMenu(controllers.MenuController.menuForm, controllers.MenuController.rmForm, controllers.MenuController.updateForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  def showMenu: Action[AnyContent] = Action {
    models.categorize()
    services.OrderService.cancelOrder()
    Ok(views.html.showMenu(MenuService.addedToMenu, controllers.BillController.billform))
  }

}
