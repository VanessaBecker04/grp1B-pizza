package forms

import java.util.Date

/**
  * Created by Hasi on 14.12.2016.
  */
case class CreateOrderHistoryForm(var customerId : Long, var customerData : String, var orderedProducts : String,
                                  var sumOfOrder: Double, var orderDate: Date)
