package controllers

import java.text.SimpleDateFormat
import java.util.Date

import forms.CreateBillForm
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService


/** Kontroller für die Rechnungserstellung einer Bestellung.
  * Created by Hasibullah Faroq on 28.11.2016.
  */
object BillController extends Controller {
  /**
    * Form Objekt für die Benutzer Daten.
    */
  val billform = Form(
    mapping(
      "CustomerID" -> longNumber, "Pizza" -> text, "Anzahl der Pizza" -> number(min = 0, max = 100),
      "Pizzagröße" -> text, "Getränk" -> text, "Anzahl der Getränk" -> number(min = 0, max = 100), "Getränkgröße" -> text,
      "Dessert" -> text, "Dessertgröße" -> text, "Anzahl der Dessert" -> number(min = 0, max = 100))(CreateBillForm.apply)(CreateBillForm.unapply))

  /** Übergibt Daten die über die Bestellübersicht(showMenu) eingegeben werden, an die Datenbank Orderbill.
    *
    * @return entweder attemptFailed, login oder showBill(Rechnung)
    */
  def addToBill: Action[AnyContent] = Action { implicit request =>
    billform.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showMenu(List.empty, formWithErrors))
      },
      userData => {
        var userID : Long = 0
        if (request2session.get("user").isDefined) {
          userID = request2session.get("user").get.toLong
        }
        val newOrder = services.OrderService.addToOrder(userID, userData.pizzaName, userData.pizzaNumber,
          userData.pizzaSize, userData.beverageName, userData.beverageNumber, userData.beverageSize,
          userData.dessertName, userData.dessertNumber)
        if (userData.pizzaNumber == 0 && userData.beverageNumber == 0 && userData.dessertNumber == 0) {
          Redirect(routes.UserController.attemptFailed("atLeastOneProduct"))
        } else {
          MenuService.setUndeleteable(userData.pizzaName, userData.pizzaNumber, userData.beverageName,
            userData.beverageNumber, userData.dessertName, userData.dessertNumber)
          val (orderedProducts, sumOfOrder) = services.OrderService.doCalculationForBill(userID, newOrder.id)
          if (request2session.get("orderedProducts").isEmpty) {
            Redirect(routes.BillController.setOrder(newOrder.id, orderedProducts.toString, sumOfOrder))
          } else {
            Redirect(routes.BillController.setOrder(newOrder.id, request2session.get("orderedProducts").get + ", " + orderedProducts.toString, request2session.get("sumOfOrder").get.toDouble + sumOfOrder))
          }
        }
      })
  }

  def setOrder(orderID: Long, orderedProducts: String, sumOfOrder: Double): Action[AnyContent] = Action { implicit request =>
    var customerData: String = ""
    if (request2session.get("user").isDefined) {
      customerData = request2session.get("forename").get.toString + " " + request2session.get("name").get.toString + ", " + request2session.get("address").get.toString + ", " + request2session.get("zipcode").get.toString + " " + request2session.get("city").get.toString
    }
    val orderDate = new Date()
    val sdf: SimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy")
    val currentDate: String = sdf.format(orderDate)
    var deliveryTime: Double = 0
    if (request2session.get("user").isDefined) {
      deliveryTime = services.OrderService.calculateDeliveryTime(request2session.get("zipcode").get.toInt)
    }
    if (request2session.get("user").isEmpty) {
      Redirect(routes.UserController.attemptFailed("loginrequired")).withSession(
        "orderID" -> orderID.toString,
        "orderedProducts" -> orderedProducts.toString,
        "sumOfOrder" -> sumOfOrder.toString,
        "customerData" -> customerData.toString,
        "currentDate" -> currentDate.toString,
        "deliveryTime" -> deliveryTime.toString
        )
    } else {
      Redirect(routes.BillController.showBill()).withSession(
        request.session +
          ("orderID" -> orderID.toString) +
          ("orderedProducts" -> orderedProducts.toString) +
          ("sumOfOrder" -> sumOfOrder.toString) +
          ("customerData" -> customerData.toString) +
          ("currentDate" -> currentDate.toString) +
          ("deliveryTime" -> deliveryTime.toString)
      )
    }
  }

  /** Leitet Kunden zum Warenkorb weiter.
    *
    * @return showBill(Warenkorb)
    */
  def showBill: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.showBill())
  }

  /** Bricht den Bestellvorgang ab und leitet den Kunden zur Bestellübersicht weiter.
    *
    * @return showMenu(Bestellübersicht)
    */
  def cancelOrder: Action[AnyContent] = Action { implicit request =>
    MenuService.setUndeleteable(null, 0, null, 0, null, 0)
    services.OrderService.cancelOrder()
    Redirect(routes.MenuController.showMenu()).withSession(
      request.session
        .-("orderID")
        .-("orderedProducts")
        .-("sumOfOrder")
        .-("customerData")
        .-("currentDate")
        .-("orderedProducts")
    )
  }
}

