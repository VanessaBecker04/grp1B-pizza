package controllers

import forms.{CreateMenuForm, CreateRemoveFromMenuForm}
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
      "Name" -> text, "Price" -> of[Double], "Category" -> text)(CreateMenuForm.apply)(CreateMenuForm.unapply))
  val rmForm = Form(mapping("Id" -> longNumber)(CreateRemoveFromMenuForm.apply)(CreateRemoveFromMenuForm.unapply))

  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */

  def addToMenu: Action[AnyContent] = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(formWithErrors, null))
      },
      userData => {
        val newProduct = services.MenuService.addToMenu(userData.name, userData.price, userData.category)
        Redirect(routes.MenuController.editMenu())
      })
  }

  def rmFromMenu: Action[AnyContent] = Action { implicit request =>
    rmForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, formWithErrors))
      },
      userData => {
        val rm = services.MenuService.rmFromMenu(userData.id)
        Redirect(routes.MenuController.editMenu())
      })
  }

  def editMenu: Action[AnyContent] = Action {
    Ok(views.html.editMenu(controllers.MenuController.menuForm, controllers.MenuController.rmForm))
  }

  def showMenu: Action[AnyContent] = Action {
    services.OrderService.cancelOrder()
    Ok(views.html.showMenu(MenuService.addedToMenu, controllers.BillController.billform))
  }

}
