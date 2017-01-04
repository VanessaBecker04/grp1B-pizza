package models

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by Hasibullah Faroq on 13.12.2016.
  * Bestellverlauf Objekt, die genutzt wird um vorhandene Daten passend umzuformen, damit diese in die Datenbank Order-
  * history hinzugefügt werden können.
  */
object OrderProcess {
  val currentDate: String = sdf.format(orderDate)
  var orderID: Long = 0
  var customerID: Long = 0
  var customerData: String = ""
  var orderedProducts: StringBuilder = new StringBuilder
  var sumOfOrder: Double = 0
  var orderDate = new Date()
  var sdf: SimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy")


}

/** übergibt Werte an das Orderprocess Objekt
  *
  * @param orderID         Bestellnummer
  * @param customerID      Kundennummer des Kunden, der die Bestellung aufgegeben hat
  * @param orderedProducts bestellte Produkte
  * @param sumOfOrder      Gesamtsumme der Bestellung
  */
case class setOrder(orderID: Long, customerID: Long, orderedProducts: StringBuilder, sumOfOrder: Double) {
  OrderProcess.orderID = orderID
  OrderProcess.customerID = customerID
  val customerList = services.UserService.registeredUsers
  for (c <- customerList) {
    if (c.id == customerID) {
      OrderProcess.customerData = c.forename + " " + c.name + " " + c.address + " " + c.zipcode + " " + c.city
      calculateDeliveryTime(c.zipcode, c.name)
    }
  }
  OrderProcess.orderedProducts = orderedProducts
  OrderProcess.sumOfOrder = sumOfOrder
}
