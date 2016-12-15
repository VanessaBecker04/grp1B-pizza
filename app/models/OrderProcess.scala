package models

import java.util.Date

/**
  * Created by Hasi on 13.12.2016.
  */
object OrderProcess {

  var customerId: Long = 0
  var customerData: String = ""
  var orderedProducts: StringBuilder = new StringBuilder
  var sumOfOrder: Double = 0
  var orderDate = new Date()

}

case class setOrder(customerId: Long, orderedProducts: StringBuilder, sumOfOrder: Double) {
  OrderProcess.customerId = customerId
  val customerList = services.UserService.registeredUsers
  for(c <- customerList) {
    if (c.id == customerId) {
      OrderProcess.customerData = c.forename + " " + c.name + " " + c.address + " " + c.zipcode + " " + c.city
    }
  }
  OrderProcess.orderedProducts = orderedProducts
  OrderProcess.sumOfOrder = sumOfOrder
}
