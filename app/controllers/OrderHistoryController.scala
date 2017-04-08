package controllers

import forms.UserIDForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Created by Hasibullah Faroq, Maximilian Oettl on 14.12.2016.
  */
object OrderHistoryController extends Controller {

  /** Form Objekt für die Benutzerdaten.
    *
    */
  val userOrdersForm = Form {
    mapping(
      "CustomerID" -> longNumber
    )(UserIDForm.apply)(UserIDForm.unapply)
  }

  /** Fügt ein neuen Bestellverlauf des Kunden in das System ein.
    *
    * @return erwartete Lieferzeit
    */
  def addToHistory(): Action[AnyContent] = Action {
    services.OrderHistoryService.addToHistory(models.OrderProcess.orderID, models.OrderProcess.customerID, models.OrderProcess.customerData, models.OrderProcess.orderedProducts.toString(), models.OrderProcess.sumOfOrder, models.OrderProcess.currentDate)
    for (s <- services.MenuService.addedToMenu) {
      if (s.name.equals(models.UndeleteableProducts.pizza)) {
        services.MenuService.setProductOrdered(s.id)
      }
      if (s.name.equals(models.UndeleteableProducts.beverage)) {
        services.MenuService.setProductOrdered(s.id)
      }
      if (s.name.equals(models.UndeleteableProducts.dessert)) {
        services.MenuService.setProductOrdered(s.id)
      }
    }
    Redirect(routes.OrderHistoryController.showDeliveryTime())
  }

  def showOrdersUser(): Action[AnyContent] = Action { implicit request =>
    if (request2session.get("user").isDefined) {
      var sumOfOrders: Double = 0
      var numberOfOrders: Int = 0
      val orders = services.OrderHistoryService.showOrdersUser(request2session.get("user").get.toLong)
      for (order <- orders) {
        sumOfOrders = sumOfOrders + order.sumOfOrder
        numberOfOrders = numberOfOrders + 1
      }
      val averageOrderSum: Double = Math.round((sumOfOrders / numberOfOrders) * 100.0) / 100.0
      sumOfOrders = Math.round(sumOfOrders * 100.0) / 100.0
      Ok(views.html.showOrdersUser(orders, sumOfOrders, averageOrderSum))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  def showOrdersEmployee(): Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      var sumOfOrders: Double = 0
      var numberOfOrders: Int = 0
      val orders = services.OrderHistoryService.showOrdersEmployee
      for (order <- orders) {
        sumOfOrders = sumOfOrders + order.sumOfOrder
        numberOfOrders = numberOfOrders + 1
      }
      val averageOrderSum: Double = Math.round((sumOfOrders / numberOfOrders) * 100.0) / 100.0
      sumOfOrders = Math.round(sumOfOrders * 100.0) / 100.0
      Ok(views.html.showOrdersEmployee(orders, sumOfOrders, averageOrderSum))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Zeigt die erwartete Lieferzeit der aufgegebenen Bestellung an.
    *
    * @return erwartete Lieferzeit
    */
  def showDeliveryTime: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.deliveryTime())
  }

  def showOrdersEmployeeU: Action[AnyContent] = Action { implicit request =>
    userOrdersForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editOrders(services.UserService.registeredUsers, formWithErrors))
      },
      userData => {
        var sumOfOrders: Double = 0
        var numberOfOrders: Int = 0
        val orders = services.OrderHistoryService.showOrdersUser(userData.customerID)
        for (order <- orders) {
          sumOfOrders = sumOfOrders + order.sumOfOrder
          numberOfOrders = numberOfOrders + 1
        }
        val averageOrderSum: Double = Math.round((sumOfOrders / numberOfOrders) * 100.0) / 100.0
        sumOfOrders = Math.round(sumOfOrders * 100.0) / 100.0
        Ok(views.html.showOrdersUser(orders, sumOfOrders, averageOrderSum))
      })
  }

  def editOrders: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editOrders(services.UserService.registeredUsers, controllers.OrderHistoryController.userOrdersForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }
}