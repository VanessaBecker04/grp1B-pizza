package controllers

import forms.CreateBillForm
import models.{Bill, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}


/**
  * Created by Hasi on 28.11.2016.
  */
object BillController extends Controller {

  val billForm = Form(mapping(
    "CustomerID" -> longNumber, "Pizza" -> text, "Beverage" -> text, "Dessert" -> text,
    "PizzaSize" -> boolean, "BeverageSize" -> boolean) (CreateBillForm.apply) (CreateBillForm.unapply)
  )

  def createBill: Action[AnyContent] = Action { implicit c =>
    billForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showMenu(List.empty,formWithErrors))
      },
      userData => {
        val newbill = forms.CreateBillForm(userData.customerID, userData.pizza,
          userData.beverage, userData.dessert, userData.pizzaSize, userData.pizzaSize)
        Redirect(routes.BillController.createBill())
      }
    )
  }

}
