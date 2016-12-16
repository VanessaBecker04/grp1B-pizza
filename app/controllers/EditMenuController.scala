package controllers

import forms.CreateMenuForm
import models.Menu
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService
import play.api.data.format.Formats._

/**
  * Created by Hasi on 21.11.2016.
  */
object EditMenuController extends Controller {

  /**
    * Form object for user data.
    */
  val menuForm = Form(
    mapping(
      "Name" -> text, "Price" -> of[Double], "Category" -> text)(CreateMenuForm.apply)(CreateMenuForm.unapply))
  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */

  def addToMenu: Action[AnyContent] = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(formWithErrors))
      },
      userData => {
        val newProduct = services.MenuService.addToMenu(userData.name, userData.price, userData.category)
        Redirect(routes.EditMenuController.showMenu())
      })
  }

  def editMenu: Action[AnyContent] = Action {
    Ok(views.html.editMenu(controllers.EditMenuController.menuForm))
  }

  def showMenu: Action[AnyContent] = Action {
    Ok(views.html.showMenu(MenuService.addedToMenu, controllers.BillController.billform))
  }

}
