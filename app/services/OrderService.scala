package services

import controllers.UserController.request2session
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
  def doCalculationForBill(cart: Bill) = {

    val bill: Bill = cart
    val menu = services.MenuService.addedToMenu

    var pizzaSum: Double = 0
    var beverageSum: Double = 0
    var dessertSum: Double = 0
    var wholeSum: Double = 0
    val orderedProducts: StringBuilder = new StringBuilder

    if (bill.pizzaName != null && bill.pizzaNumber > 0) {
      orderedProducts.append(bill.pizzaNumber + "x ")
      orderedProducts.append(bill.pizzaName + " ")
      orderedProducts.append("(" + bill.pizzaSize + ")")
      if (bill.beverageNumber > 0 || bill.dessertNumber > 0) {
        orderedProducts.append(", ")
      }
      for (m <- menu) {
        if (m.name.equals(bill.pizzaName)) {
          if (bill.pizzaSize.equals("medium")) {
            pizzaSum += m.price
            pizzaSum = (pizzaSum * 27) * bill.pizzaNumber
          }
          if (bill.pizzaSize.equals("large")) {
            pizzaSum += m.price
            pizzaSum = (pizzaSum * 32) * bill.pizzaNumber
          }
          if (bill.pizzaSize.equals("xl")) {
            pizzaSum += m.price
            pizzaSum = (pizzaSum * 36) * bill.pizzaNumber
          }
        }
      }
    } else {
    }
    if (bill.beverageName != null && bill.beverageNumber > 0) {
      orderedProducts.append(bill.beverageNumber + "x ")
      orderedProducts.append(bill.beverageName)
      orderedProducts.append(" (" + bill.beverageSize + ")")
      if (bill.dessertNumber > 0) {
        orderedProducts.append(", ")
      }
      for (m <- menu) {
        if (m.name.equals(bill.beverageName)) {
          if (bill.beverageSize.equals("0.5l")) {
            beverageSum += m.price
            beverageSum = (beverageSum * 5) * bill.beverageNumber
          }
          if (bill.beverageSize.equals("0.75l")) {
            beverageSum += m.price
            beverageSum = (beverageSum * 7.5) * bill.beverageNumber
          }
          if (bill.beverageSize.equals("1.0l")) {
            beverageSum += m.price
            beverageSum = (beverageSum * 10) * bill.beverageNumber
          }
        }
      }
    } else {
    }
    if (bill.dessertName != null && bill.dessertNumber > 0) {
      orderedProducts.append(bill.dessertNumber + "x ")
      orderedProducts.append(bill.dessertName)
      for (m <- menu) {
        if (m.name.equals(bill.dessertName)) {
          dessertSum += m.price
          dessertSum = dessertSum * bill.dessertNumber
        }
      }
    } else {
    }
    wholeSum += (Math.round((pizzaSum + beverageSum + dessertSum) * 100.0) / 100.0)
    (orderedProducts, wholeSum)
  }

  /**
    * Berechnung der Lieferzeit.
    */
  def calculateDeliveryTime(customerZIP: Int): Double = {
    var km: Int = 0
    var kmpm: Double = DeliveryTime.kilometersperminute

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
