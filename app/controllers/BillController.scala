package controllers

import java.text.SimpleDateFormat
import java.util.Date

import forms.{CreateBillForm, ProductForm}
import models.{Bill, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

import scala.collection.mutable.ListBuffer


/** Kontroller für die Rechnungserstellung einer Bestellung.
  * Created by Hasibullah Faroq on 28.11.2016.
  */
object BillController extends Controller {
  /**
    * Form Objekt für die Benutzer Daten.
    */
  val billform: Form[CreateBillForm] = Form(
    mapping(
      "products" -> list(mapping(
        "name" -> text,
        "size" -> text,
        "number" -> number)(ProductForm.apply)(ProductForm.unapply))
    )(CreateBillForm.apply)(CreateBillForm.unapply))

  /** Übergibt Daten die über die Bestellübersicht(showMenu) eingegeben werden, an die Datenbank Orderbill.
    *
    * @return entweder attemptFailed, login oder showBill(Rechnung)
    */
  def addToBill: Action[AnyContent] = Action { implicit request =>
    var order = new ListBuffer[Product]()
      billform.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.showMenu(List.empty, formWithErrors))
      },
      userData => {
        var userData2 = CreateBillForm(List(ProductForm("Pizza Margarita", "medium", 5), ProductForm("Sprite", "1.0l", 2)))
        Console.println("userData: " + userData2)
        for (p <- userData2.products) {
          order += Product(p.name, p.size, p.number)
          if (p.number == 0) {
            Redirect(routes.UserController.attemptFailed("atLeastOneProduct"))
          }
        }
         // MenuService.setUndeleteable(userData.pizzaName, userData.pizzaNumber, userData.beverageName,
           // userData.beverageNumber, userData.dessertName, userData.dessertNumber)
        Console.println("Produkte: " + order.toList)
        val cart: Bill = Bill(order.toList)
        val (orderedProducts, sumOfOrder) = services.OrderService.doCalculationForBill(cart)
        if (request2session.get("orderedProducts").isEmpty) {
          Redirect(routes.BillController.setOrder(orderedProducts, sumOfOrder))
        } else {
          Redirect(routes.BillController.setOrder(request2session.get("orderedProducts").get + ", " + orderedProducts, request2session.get("sumOfOrder").get.toDouble + sumOfOrder))
        }
      })
  }

  def setOrder(orderedProducts: String, sumOfOrder: Double): Action[AnyContent] = Action { implicit request =>
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
        "orderedProducts" -> orderedProducts.toString,
        "sumOfOrder" -> sumOfOrder.toString,
        "customerData" -> customerData.toString,
        "currentDate" -> currentDate.toString,
        "deliveryTime" -> deliveryTime.toString
        )
    } else {
      Redirect(routes.BillController.showBill()).withSession(
        request.session +
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
    Redirect(routes.MenuController.showMenu()).withSession(
      request.session
        .-("orderedProducts")
        .-("sumOfOrder")
        .-("customerData")
        .-("currentDate")
        .-("orderedProducts")
    )
  }
}

