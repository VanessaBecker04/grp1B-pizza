package controllers

import forms.{LongForm, NewStatusForm}
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, text}
import play.api.mvc.{Action, AnyContent, Controller}
import scala.collection.mutable.ListBuffer

/** Controller for the Order.
  *
  * @author Maximilian Öttl, Hasibullah Faroq
  */
object OrderController extends Controller {

  /** Form object for the user data */
  val userOrdersForm = Form {
    mapping(
      "CustomerID" -> longNumber
    )(LongForm.apply)(LongForm.unapply)
  }

  val newStatusForm = Form(mapping(
    "BestellID" -> longNumber,
    "Neuer Status" -> text
  )(NewStatusForm.apply)(NewStatusForm.unapply))


  /** Adds a new order to the database.
    *
    * @return Expected delivery time
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

  /** Shows all orders from the user plus the total/average sum of his orders.
    *
    * @return orders, sumOfOrders, averageOrderSum
    */
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

  /** Shows all orders in the database plus the total/average sum of all orders.
    * User must be an employee otherwise a permission denied message will be shown.
    *
    * @return orders, sumOfOrders, averageOrderSum, newStatusForm or Error page
    */
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

  /** Shows all orders from a specified user in the database plus the total/average sum of his orders.
    *
    * @return orders, sumOfOrders and averageOrderSum
    */
  def showOrdersEmployeeU: Action[AnyContent] = Action { implicit request =>
    userOrdersForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
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

  /** Shows the delivery time of the new order.
    *
    * @return expected delivery time
    */
  def showDeliveryTime: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.deliveryTime())
  }

  /** Shows the page with all order information.
    *
    * @return All orders
    */
  def editOrders: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editOrders(services.UserService.registeredUsers, controllers.OrderController.userOrdersForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Cancels an order and deletes them from the OrderHistory table.
    *
    * @param orderID order id to be deleted
    */
  def cancelOrderHistory(orderID: Long): Action[AnyContent] = Action { implicit request =>
    services.OrderService.rmFromHistory(orderID)
    Redirect(routes.OrderController.showOrdersUser())
  }

  /** Sets the status of the order.
    *
    * @return status
    */
  def setStatusForOrder(): Action[AnyContent] = Action { implicit request =>
    newStatusForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
    userData => {
      services.OrderService.setStatusForOrder(userData.orderID, userData.status)
      Redirect(routes.OrderController.showOrdersEmployee())
    })
  }
}