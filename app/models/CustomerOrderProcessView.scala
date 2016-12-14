package models

import java.util.Date

/**
  * Created by Hasi on 13.12.2016.
  */
object CustomerOrderProcessView {

  var customerId: Long = 0
  var customerData: String = ""
  var orderedProducts: StringBuilder = new StringBuilder
  var sumOfOrder: Double = 0
  var orderDate = new Date()

}

case class setView(customerId: Long, orderedProducts: StringBuilder, sumOfOrder: Double) {
  CustomerOrderProcessView.customerId = customerId
  val customerList = services.UserService.registeredUsers
  for(c <- customerList) {
    if (c.id == customerId) {
      CustomerOrderProcessView.customerData = c.forename + " " + c.name + " " + c.address + " " + c.zipcode + " " + c.city
    }
  }
  CustomerOrderProcessView.orderedProducts = orderedProducts
  CustomerOrderProcessView.sumOfOrder = sumOfOrder
}
