package controllers

import forms.CreateBillForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}



/**
  * Created by Hasi on 28.11.2016.
  */
object BillController extends Controller {
  var userID : Long = _
  val billform = Form(
    mapping(
      "CustomerID" -> longNumber, "PizzaName" -> text, "PizzaNumber" -> number,
      "PizzaSize" -> text, "BeverageName" -> text, "BeverageNumber" -> number, "BeverageSize" -> text,
      "DessertName" -> text, "DessertNumber" -> number)(CreateBillForm.apply)(CreateBillForm.unapply))



  def addToBill: Action[AnyContent] = Action { implicit request =>
    billform.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showMenu(List.empty,formWithErrors))
      },
      userData => {
        val newOrder = services.OrderService.addToOrder(userData.customerId, userData.pizzaName, userData.pizzaNumber,
          userData.pizzaSize, userData.beverageName, userData.beverageNumber, userData.beverageSize,
          userData.dessertName, userData.dessertNumber)
        Redirect(routes.BillController.showBill())
      })
  }

  def showBill : Action[AnyContent] = Action {
    Ok(views.html.showBill(services.OrderService.addedToOrder))
  }

  def setUserID(userIDgiven : Long) = {
    userID = userIDgiven
  }

  def getuserID : Long = userID


}

