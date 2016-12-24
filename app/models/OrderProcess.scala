package models

import java.text.{DateFormat, SimpleDateFormat}
import java.util.{Calendar, Date, GregorianCalendar, Locale}

/**
  * Created by Hasi on 13.12.2016.
  */
object OrderProcess {
  var orderID: Long = 0
  var customerID: Long = 0
  var customerData: String = ""
  var orderedProducts: StringBuilder = new StringBuilder
  var sumOfOrder: Double = 0
  var orderDate = new Date()
  var sdf: SimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy")
  val currentDate: String = sdf.format(orderDate)


}

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
