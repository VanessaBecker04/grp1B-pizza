package models

import java.util.Date

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
