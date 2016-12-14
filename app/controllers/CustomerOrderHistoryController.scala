package controllers

import forms.CreateCustomerOrderHistoryForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.format.Formats._

/**
  * Created by Hasi on 14.12.2016.
  */
object CustomerOrderHistoryController extends Controller {

  val cohform = Form(
    mapping(
      "CustomerId" -> longNumber, "CustomerData" -> text, "OrderedProducts" -> text,
      "SumOfOrder" -> of[Double], "OrderDate" -> date)(CreateCustomerOrderHistoryForm.apply)(CreateCustomerOrderHistoryForm.unapply))

  def addToHistory: Action[AnyContent] = Action { implicit request =>
    cohform.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.orderConfirm(formWithErrors))
      },
      userData => {
        val newOrder = services.CustomerOrderHistoryService.addToHistory(userData.customerId, userData.customerData,
          userData.orderedProducts, userData.sumOfOrder, userData.orderDate)
        Redirect(routes.CustomerOrderHistoryController.orderConfirm())
      })
  }

  def orderConfirm : Action[AnyContent] = Action {
    Ok(views.html.orderConfirm(cohform))
  }



}
