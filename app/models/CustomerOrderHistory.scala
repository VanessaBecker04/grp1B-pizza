package models

import java.util.Date

/**
  * Created by Hasi on 14.12.2016.
  */
case class CustomerOrderHistory(var customerId : Long, var customerData : String, var orderedProducts : String,
                                var sumOfOrder: Double, var orderDate: Date)
