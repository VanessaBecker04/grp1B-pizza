package forms

import java.util.Date

/**
  * Created by Hasi on 14.12.2016.
  */
case class CreateOrderHistoryForm(orderID: Long, customerID: Long, customerData: String, orderedProducts: String,
                                  sumOfOrder: Double, orderDate: Date)
