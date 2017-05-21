package controllers

import forms.{LongForm, NewStatusForm}
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, text}
import play.api.mvc.{Action, AnyContent, Controller}
import scala.collection.mutable.ListBuffer

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
    )(LongForm.apply)(LongForm.unapply)
  }

  val newStatusForm = Form(mapping(
    "BestellID" -> longNumber,
    "Neuer Status" -> text
  )(NewStatusForm.apply)(NewStatusForm.unapply))


  /** Fügt ein neuen Bestellverlauf des Kunden in das System ein.
    *
    * @return erwartete Lieferzeit
    */
  def addToHistory(): Action[AnyContent] = Action { implicit request =>
    services.OrderService.addToHistory(
      request2session.get("user").get.toLong,
      request2session.get("customerData").get,
      request2session.get("orderedProducts").get,
      request2session.get("sumOfOrder").get.toDouble,
      request2session.get("currentDate").get
    )
    var products = new ListBuffer[Long]
    for (s <- services.MenuService.listOfOrderableProducts) {
      if (request2session.get("orderedProducts").get.contains(s.name) && !s.ordered) {
        products += s.id
      }
    }
    services.MenuService.setProductOrdered(products.toList)
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
      Ok(views.html.showOrdersEmployee(orders, sumOfOrders, averageOrderSum, newStatusForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  def showOrdersEmployeeU: Action[AnyContent] = Action { implicit request =>
    userOrdersForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editOrders(services.UserService.registeredUsers, formWithErrors))
      },
      userData => {
        var sumOfOrders: Double = 0
        var numberOfOrders: Int = 0
        val orders = services.OrderService.showOrdersUser(userData.value)
        for (order <- orders) {
          sumOfOrders = sumOfOrders + order.sumOfOrder
          numberOfOrders = numberOfOrders + 1
        }
        val averageOrderSum: Double = Math.round((sumOfOrders / numberOfOrders) * 100.0) / 100.0
        sumOfOrders = Math.round(sumOfOrders * 100.0) / 100.0
        Ok(views.html.showOrdersUser(orders, sumOfOrders, averageOrderSum))
      })
  }

  /** Zeigt die erwartete Lieferzeit der aufgegebenen Bestellung an.
    *
    * @return erwartete Lieferzeit
    */
  def showDeliveryTime: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.deliveryTime())
  }

  def editOrders: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editOrders(services.UserService.registeredUsers, controllers.OrderController.userOrdersForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  def cancelOrderHistory(orderID: Long): Action[AnyContent] = Action { implicit request =>
    services.OrderService.rmFromHistory(orderID)
    Redirect(routes.OrderController.showOrdersUser())
  }

  def setStatusForOrder(): Action[AnyContent] = Action { implicit request =>
    newStatusForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showOrdersEmployee(List.empty, 0, 0, formWithErrors))
      },
    userData => {
      services.OrderService.setStatusForOrder(userData.orderID, userData.status)
      Redirect(routes.OrderController.showOrdersEmployee())
    })
  }
}