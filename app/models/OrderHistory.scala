package models

import java.util.Date

/**
  * Created by Hasi on 14.12.2016.
  */
case class OrderHistory(var orderID: Long, var customerID : Long, var customerData : String, var orderedProducts : String,
                        var sumOfOrder: Double, var orderDate: Date)
