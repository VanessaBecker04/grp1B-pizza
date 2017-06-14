package services

import dbaccess.{OrderDao, OrderDaoT}
import models.{Bill, Company, DeliveryTime, OrderHistory}

/** Class for service to invoice-related actions (orderbill).
  *
  * @author Maximilian Öttl, Hasibullah Faroq
  */

trait OrderServiceT {

  val orderDao: OrderDaoT = OrderDao

  /** Calculation of the total sum of the order.
    *
    * @param cart Cart ordered products
    * @return     Total sum of the order.
    */
  def doCalculationForBill(cart: Bill): (String, Double) = {

    val menu = services.MenuService.listOfOrderableProducts
    var count = 0
    var wholeSum: Double = 0
    val orderedProducts: StringBuilder = new StringBuilder
    for (p <- cart.products) {
      count += 1
      orderedProducts.append(p.number + "x " + p.name + " " + "(" + p.size + ")" + (if(count < cart.products.length && cart.products.length > 1) ", " else ""))
      for (m <- menu) {
        if (m.name.equals(p.name)) {
          p.size match {
            case "medium" => wholeSum += m.price * 27 * p.number
            case "large" => wholeSum += m.price * 32 * p.number
            case "xl" => wholeSum += m.price * 36 * p.number
            case "0.5l" => wholeSum += m.price * 5 * p.number
            case "0.75l" => wholeSum += m.price * 7.5 * p.number
            case "1.0l" => wholeSum += m.price * 10 * p.number
            case _ => wholeSum += m.price * p.number
          }
        }
      }
    }
    (orderedProducts.toString(), wholeSum * 100.0 / 100.0)
  }

  /** Calculation of the delivery time.
    *
    * @param customerZIP  Zip of the Customer.
    * @return             Delivey time.
    */
  def calculateDeliveryTime(customerZIP: Int): Double = {
    var km: Int = 0
    val kmpm: Double = DeliveryTime.kilometersperminute

    customerZIP match {
      case Company.zip => km = 4
      case 82335 | 82343 => km = 6
      case 82340 => km = 10
      case 82061 | 82069 | 82131 => km = 12
      case 82234 => km = 14
      case 82057 | 82065 | 82229 | 82327 | 82346 => km = 16
      case _ =>
    }
    if (km == 0) {
      -1
    } else {
      km / kmpm + DeliveryTime.bakeTime
    }
  }
  /** Adds a new order from the customer to the orderhistory database.
    *
    * @param customerID      Number of the customer.
    * @param customerData    Data of the customer.
    * @param orderedProducts Ordered products.
    * @param sumOfOrder      Total sum of the order.
    * @param orderDate       Date of the order.
    * @return                New Order from the Customer.
    */
  def addToHistory(customerID: Long, customerData: String, orderedProducts: String, sumOfOrder: Double,
                   orderDate: String): OrderHistory = {
    // create User
    val newHistory = OrderHistory(-1, customerID, customerData, orderedProducts, sumOfOrder, orderDate, "in Bearbeitung")
    // persist and return User
    orderDao.addToHistory(newHistory)
  }

  /** Removes an order from the orderhistory database.
    *
    * @param id Number of the order.
    * @return True worth whether the deletion was successful.
    */
  def rmFromHistory(id: Long): Boolean = orderDao.rmFromHistory(id)

  /** Returns a list of all registered users.
    *
    * @return list of users.
    */
  def showOrdersEmployee: List[OrderHistory] = {
    orderDao.showOrdersEmployee
  }

  /** Returns a list of all orders from users.
    *
    * @param id Number of the order.
    * @return   list of users.
    */
  def showOrdersUser(id: Long): List[OrderHistory] = {
    orderDao.showOrdersUser(id)
  }

  /** Sets a new status for an order.
    *
    * @param orderID    Number of the order.
    * @param newStatus  New status of the order.
    */
  def setStatusForOrder(orderID: Long, newStatus: String): Unit = orderDao.setStatusForOrder(orderID, newStatus)
}

object OrderService extends OrderServiceT