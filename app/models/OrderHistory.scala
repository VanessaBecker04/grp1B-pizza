package models

/** Entity of the order.
  *
  * @param orderID          Number of the order.
  * @param customerID       Number of the customer.
  * @param customerData     Data of the customer.
  * @param orderedProducts  Products which were ordered by the customer.
  * @param sumOfOrder       Sum of the order.
  * @param orderDate        Data of the order.
  * @param status           Status of the order.
  *
  * @author Hasibullah Faroq
  */
case class OrderHistory(var orderID: Long, var customerID: Long, var customerData: String, var orderedProducts: String,
                        var sumOfOrder: Double, var orderDate: String, var status: String)