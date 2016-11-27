package controllers

import forms.{CreateBillForm, CreateMenuForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

/**
  * Created by Hasi on 21.11.2016.
  */
object EditMenuController extends Controller {

  /**
    * Form object for user data.
    */
  val menuForm = Form(
    mapping(
      "Name" -> text, "Price" -> number, "Category" -> text)(CreateMenuForm.apply)(CreateMenuForm.unapply))

  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */

  val billForm = Form(
    mapping(
      "Pizza" -> text, "Beverage" -> text, "Dessert" -> text, "PizzaSize" -> boolean, "BeverageSize" -> boolean)
    (CreateBillForm.apply)(CreateBillForm.unapply))


  def createBill: Action[AnyContent] = Action { implicit c =>
    billForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showBill(formWithErrors))
      },
      userData => {
        val newBill = bill(userData.pizza, userData.beverage, userData.dessert,
          userData.pizzaSize, userData.beverageSize)
        Redirect(routes.EditMenuController.createBill())
      }
    )
  }


  def bill(pizza: String, beverage: String, dessert: String, pizzaSize: Boolean, beverageSize: Boolean):
  (String, String, String, Boolean, Boolean) = {
    val tuple = (pizza, beverage, dessert, pizzaSize, beverageSize)
    tuple
  }

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
    Ok(views.html.showMenu(MenuService.addedToMenu, controllers.EditMenuController.billForm))
  }

}
