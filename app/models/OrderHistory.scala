package models

/**
  * Created by Hasibullah Faroq on 14.12.2016.
  * Bestellungs Entität
  */
case class OrderHistory(var orderID: Long, var customerID: Long, var customerData: String, var orderedProducts: String,
                        var sumOfOrder: Double, var orderDate: String, var status: String)
