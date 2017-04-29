package services

import dbaccess.{OrderDao, OrderDaoT}
import models.{Bill, Company, DeliveryTime, OrderHistory}

/** Service Klasse für Rechnungsbezogene (Orderbill) Handlungen.
  * Created by Hasibullah Faroq on 28.11.2016.
  */

trait OrderServiceT {

  val orderDao: OrderDaoT = OrderDao

  /** berechnet die Gesamtsumme der Bestellung
    *
    */
  def doCalculationForBill(cart: Bill): (String, Double) = {

    val menu = services.MenuService.addedToMenu
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

  /**
    * Berechnung der Lieferzeit.
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
  /**
    * fügt ein neue Bestellung des Kunden in die Datenbank Orderhistory ein.
    *
    * @param customerID      Kundennummer
    * @param customerData    Kundendaten
    * @param orderedProducts bestellte Produkte
    * @param sumOfOrder      Gesamtsummer der Bestellung
    * @param orderDate       Datum der Bestellung
    */
  def addToHistory(customerID: Long, customerData: String, orderedProducts: String, sumOfOrder: Double,
                   orderDate: String): OrderHistory = {
    // create User
    val newHistory = OrderHistory(-1, customerID, customerData, orderedProducts, sumOfOrder, orderDate)
    // persist and return User
    orderDao.addToHistory(newHistory)
  }

  /**
    * Entfernt eine Bestellung aus der Datenbank Orderhistory.
    *
    * @param id id der Bestellung
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromHistory(id: Long): Boolean = orderDao.rmFromHistory(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */

  def showOrdersUser(id: Long): List[OrderHistory] = {
    orderDao.showOrdersUser(id)
  }

  def showOrdersEmployee: List[OrderHistory] = {
    orderDao.showOrdersEmployee
  }
}

object OrderService extends OrderServiceT
