package controllers

import forms.IDForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

/**
  * Created by Hasibullah Faroq, Maximilian Oettl on 14.12.2016.
  */
object OrderController extends Controller {

  /** Form Objekt für die Benutzerdaten.
    *
    */
  val userOrdersForm = Form {
    mapping(
      "CustomerID" -> longNumber
    )(IDForm.apply)(IDForm.unapply)
  }

  /** Fügt ein neuen Bestellverlauf des Kunden in das System ein.
    *
    * @return erwartete Lieferzeit
    */
  def addToHistory(): Action[AnyContent] = Action { implicit request =>
    Console.println("Value of 'user' from Session logged in: " + request2session.get("user"))
    Console.println("Value of 'forename' from Session logged in: " + request2session.get("forename"))
    Console.println("Value of 'name' from Session logged in: " + request2session.get("name"))
    Console.println("Value of 'orderID' from Session logged in: " + request2session.get("orderID"))
    Console.println("Value of 'orderedProducts' from Session logged in: " + request2session.get("orderedProducts"))
    Console.println("Value of 'sumOfOrder' from Session logged in: " + request2session.get("sumOfOrder"))
    services.OrderService.addToHistory(
      request2session.get("user").get.toLong,
      request2session.get("customerData").get,
      request2session.get("orderedProducts").get,
      request2session.get("sumOfOrder").get.toDouble,
      request2session.get("currentDate").get
    )
    for (s <- services.MenuService.addedToMenu) {
      if (s.name.equals(MenuService.pizza)) {
        services.MenuService.setProductOrdered(s.id)
      }
      if (s.name.equals(MenuService.beverage)) {
        services.MenuService.setProductOrdered(s.id)
      }
      if (s.name.equals(MenuService.dessert)) {
        services.MenuService.setProductOrdered(s.id)
      }
    }
    Redirect(routes.OrderController.showDeliveryTime()).withSession(
      request.session
        .-("orderedProducts")
        .-("sumOfOrder")
        .-("customerData")
        .-("currentDate")
        .-("orderedProducts")
    )
  }

  def showOrdersUser(): Action[AnyContent] = Action { implicit request =>
    if (request2session.get("user").isDefined) {
      var sumOfOrders: Double = 0
      var numberOfOrders: Int = 0
      val orders = services.OrderService.showOrdersUser(request2session.get("user").get.toLong)
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
      val orders = services.OrderService.showOrdersEmployee
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
        val orders = services.OrderService.showOrdersUser(userData.id)
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
      Ok(views.html.editOrders(services.UserService.registeredUsers, controllers.OrderController.userOrdersForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }
}