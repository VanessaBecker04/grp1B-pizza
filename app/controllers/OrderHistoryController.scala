package controllers

import forms.UserIDForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.OrderHistoryService

/**
  * Created by Hasi on 14.12.2016.
  */
object OrderHistoryController extends Controller {

  val userOrdersForm = Form {
    mapping(
      "CustomerID" -> longNumber
    )(UserIDForm.apply)(UserIDForm.unapply)
  }

  def addToHistory(): Action[AnyContent] = Action {
    services.OrderHistoryService.addToHistory(models.OrderProcess.orderID, models.OrderProcess.customerID, models.OrderProcess.customerData, models.OrderProcess.orderedProducts.toString(), models.OrderProcess.sumOfOrder, models.OrderProcess.currentDate)
    Redirect(routes.OrderHistoryController.showDeliveryTime())
  }

  def showOrdersUser(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersUser(OrderHistoryService.showOrdersUser(models.activeUser.id)))
  }

  def showOrdersEmployee(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersEmployee(OrderHistoryService.showOrdersEmployee))
  }

  def showDeliveryTime: Action[AnyContent] = Action {
    Ok(views.html.deliveryTime())
  }

  def showOrdersEmployeeU: Action[AnyContent] = Action { implicit request =>
    userOrdersForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editOrders(services.UserService.registeredUsers, formWithErrors))
      },
      userData => {
        val orders = services.OrderHistoryService.showOrdersUser(userData.customerID)
        Ok(views.html.showOrdersUser(orders))
      })
  }

  def editOrders: Action[AnyContent] = Action {
    Ok(views.html.editOrders(services.UserService.registeredUsers, controllers.OrderHistoryController.userOrdersForm))
  }
}