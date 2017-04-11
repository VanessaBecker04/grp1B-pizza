package controllers

import java.text.SimpleDateFormat
import java.util.Date

import forms.CreateBillForm
import models.calculateDeliveryTime
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}


/** Kontroller für die Rechnungserstellung einer Bestellung.
  * Created by Hasibullah Faroq on 28.11.2016.
  */
object BillController extends Controller {
  /**
    * Form Objekt für die Benutzer Daten.
    */
  val billform = Form(
    mapping(
      "CustomerID" -> longNumber, "Pizza" -> text, "Anzahl der Pizzen" -> number(min = 0, max = 100),
      "Pizzagröße" -> text, "Getränk" -> text, "Anzahl der Getränke" -> number(min = 0, max = 100), "Getränkegröße" -> text,
      "Dessert" -> text, "Anzahl der Desserts" -> number(min = 0, max = 100))(CreateBillForm.apply)(CreateBillForm.unapply))

  /** Übergibt Daten die über die Bestellübersicht(showMenu) eingegeben werden, an die Datenbank Orderbill.
    *
    * @return entweder attemptFailed, login oder showBill(Rechnung)
    */
  def addToBill: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("user").isEmpty) {
      Redirect(routes.Application.login())
    } else {
      billform.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.showMenu(List.empty, formWithErrors))
        },
        userData => {
          val newOrder = services.OrderService.addToOrder(request2session.get("user").get.toLong, userData.pizzaName, userData.pizzaNumber,
            userData.pizzaSize, userData.beverageName, userData.beverageNumber, userData.beverageSize,
            userData.dessertName, userData.dessertNumber)
          if (userData.pizzaNumber == 0 && userData.beverageNumber == 0 && userData.dessertNumber == 0) {
            Redirect(routes.UserController.attemptFailed("atLeastOneProduct"))
          } else {
            models.setUndeleteable(userData.pizzaName, userData.pizzaNumber, userData.beverageName,
              userData.beverageNumber, userData.dessertName, userData.dessertNumber)
            services.OrderService.doCalculationForBill(request2session.get("user").get.toLong, newOrder.id)
            Redirect(routes.BillController.showBill()).withSession(
              "orderId" -> newOrder.id.toString
            )
          }
        })
    }
  }

  def setOrder(orderedProducts: StringBuilder, sumOfOrder: Double): Action[AnyContent] = Action { implicit request =>
    val customerList = services.UserService.registeredUsers
    var customerData: String = ""
    var orderDate = new Date()
    var sdf: SimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy")
    val currentDate: String = sdf.format(orderDate)
    for (c <- customerList) {
      if (c.id == request2session.get("user").get.toLong) {
        customerData = c.forename + " " + c.name + " " + c.address + " " + c.zipcode + " " + c.city
        calculateDeliveryTime(c.zipcode, c.name)
      }
    }
    Redirect(routes.BillController.showBill()).withSession(
      "orderedProducts" -> orderedProducts.toString,
      "sumOfOrder" -> sumOfOrder.toString,
      "customerData" -> customerData.toString,
      "currentDate" -> currentDate.toString
    )
  }

  /** Leitet Kunden zur Rechnung weiter.
    *
    * @return showBill(Rechnung)
    */
  def showBill: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.showBill())
  }

  /** Bricht den Bestellvorgang ab und leitet den Kunden zur Bestellübersicht weiter.
    *
    * @return showMenu(Bestellübersicht)
    */
  def cancelOrder: Action[AnyContent] = Action {
    models.setUndeleteable(null, 0, null, 0, null, 0)
    services.OrderService.cancelOrder()
    Redirect(routes.MenuController.showMenu())
  }
}

