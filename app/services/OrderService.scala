package services

import controllers.UserController.request2session
import dbaccess.{OrderDao, OrderDaoT}
import models.Bill

/** Service Klasse für Rechnungsbezogene (Orderbill) Handlungen.
  * Created by Hasibullah Faroq on 28.11.2016.
  */

trait OrderServiceT {

  val orderDao: OrderDaoT = OrderDao

  /**
    * Fügt eine neue Rechnung zu der Datenbank Orderbill hinzu.
    *
    * @param customerID     Kundennummer des Kunden der die Bestellung aufgibt
    * @param pizzaName      Name der Pizza die bestellt wird
    * @param pizzaNumber    Anzahl der bestellten Pizzen
    * @param pizzaSize      Größe der Pizza die bestellt wird
    * @param beverageName   Name des Getränks, das bestellt wird
    * @param beverageNumber Anzahl der bestellten Getränke
    * @param beverageSize   Größe des Getränks, das bestellt wird
    * @param dessertName    Name des Desserts, das bestellt wird
    * @param dessertNumber  Anzahl der bestellten Desserts
    * @return das Bill Objekt
    */
  def addToOrder(customerID: Long, pizzaName: String, pizzaNumber: Int, pizzaSize: String, beverageName: String,
                 beverageNumber: Int, beverageSize: String, dessertName: String, dessertNumber: Int): Bill = {
    // create User
    val newOrder = Bill(-1, customerID, pizzaName, pizzaNumber, pizzaSize, beverageName,
      beverageNumber, beverageSize, dessertName, dessertNumber)
    // persist and return User
    orderDao.addToOrder(newOrder)
  }

  /**
    * entfernt die Rechnungs Daten von der Datenbank Orderbill für id
    *
    * @param id Oderbill id
    */
  def rmFromOrder(id: Long): Boolean = orderDao.rmFromOrder(id)

  /** berechnet die Gesamtsumme der Bestellung
    *
    */
  def doCalculationForBill(id: Long, orderID: Long): Unit = {

    val bill = addedToOrder
    val menu = services.MenuService.addedToMenu

    var pizzaSum: Double = 0
    var beverageSum: Double = 0
    var dessertSum: Double = 0
    var wholeSum: Double = 0
    var orderedProducts: StringBuilder = new StringBuilder


    for (c <- bill) {
      if (c.customerID.equals(id) && c.id.equals(orderID)) {
        if (c.pizzaName != null && c.pizzaNumber > 0) {
          orderedProducts.append(c.pizzaNumber + "x ")
          orderedProducts.append(c.pizzaName + " ")
          orderedProducts.append("(" + c.pizzaSize + ")")
          if (c.beverageNumber > 0 || c.dessertNumber > 0) {
            orderedProducts.append(", ")
          }
          for (m <- menu) {
            if (m.name.equals(c.pizzaName)) {
              if (c.pizzaSize.equals("medium")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 27) * c.pizzaNumber
              }
              if (c.pizzaSize.equals("large")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 32) * c.pizzaNumber
              }
              if (c.pizzaSize.equals("xl")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 36) * c.pizzaNumber
              }
            }
          }
        } else {
        }
        if (c.beverageName != null && c.beverageNumber > 0) {
          orderedProducts.append(c.beverageNumber + "x ")
          orderedProducts.append(c.beverageName)
          orderedProducts.append(" (" + c.beverageSize + ")")
          if (c.dessertNumber > 0) {
            orderedProducts.append(", ")
          }
          for (m <- menu) {
            if (m.name.equals(c.beverageName)) {
              if (c.beverageSize.equals("0.5l")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 5) * c.beverageNumber
              }
              if (c.beverageSize.equals("0.75l")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 7.5) * c.beverageNumber
              }
              if (c.beverageSize.equals("1.0l")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 10) * c.beverageNumber
              }
            }
          }
        } else {
        }
        if (c.dessertName != null && c.dessertNumber > 0) {
          orderedProducts.append(c.dessertNumber + "x ")
          orderedProducts.append(c.dessertName)
          for (m <- menu) {
            if (m.name.equals(c.dessertName)) {
              dessertSum += m.price
              dessertSum = dessertSum * c.dessertNumber
            }
          }
        } else {
        }
        wholeSum += (Math.round((pizzaSum + beverageSum + dessertSum) * 100.0) / 100.0)
      }
    }
    controllers.BillController.setOrder(orderedProducts, wholeSum)
  }

  /**
    * Gibt eine Liste zurück mit allen vorhandenen Rechnungen
    *
    * @return eine liste von Bill objekten.
    */
  def addedToOrder: List[Bill] = {
    orderDao.addedToOrder
  }

  /**
    * Entfernt Rechnung, wenn Bestellung abgebrochen wurde.
    */
  def cancelOrder() = {
    orderDao.cancelOrder()
  }
}

object OrderService extends OrderServiceT
