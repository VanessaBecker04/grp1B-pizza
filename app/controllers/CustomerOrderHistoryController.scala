package controllers

import forms.CreateCustomerOrderHistoryForm
import models.CustomerOrderHistory
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.format.Formats._

/**
  * Created by Hasi on 14.12.2016.
  */
object CustomerOrderHistoryController extends Controller {

  def addToHistory(): Action[AnyContent] = Action {
        services.CustomerOrderHistoryService.addToHistory(models.CustomerOrderProcessView.customerId,
          models.CustomerOrderProcessView.customerData, models.CustomerOrderProcessView.orderedProducts.toString(),
          models.CustomerOrderProcessView.sumOfOrder, models.CustomerOrderProcessView.orderDate)
    Redirect(routes.Application.index())
  }

}
