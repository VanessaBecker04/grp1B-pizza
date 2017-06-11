package forms

import java.util.Date

/**
  * Form containing data to create an orderprocess for a customer. (Bill)
  *
  * @author Hasibullah Faroq
  *
  * @param orderID         Number of the order.
  * @param customerID      Number of the customer.
  * @param customerData    Data of the customer.
  * @param orderedProducts Ordered products.
  * @param sumOfOrder      Total of the order.
  * @param orderDate       Date of the order.
  */
case class CreateOrderHistoryForm(orderID: Long, customerID: Long, customerData: String, orderedProducts: String,
                                  sumOfOrder: Double, orderDate: Date)