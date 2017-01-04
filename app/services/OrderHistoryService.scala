package services

import dbaccess.{OrderHistoryDao, OrderHistoryDaoT}
import models.OrderHistory

/**
  * Created by Hasibullah Faroq, Maximilian Öttl on 14.12.2016.
  */

trait OrderHistoryServiceT {

  val orderHistoryDao: OrderHistoryDaoT = OrderHistoryDao

  /**
    * fügt ein neue Bestellung des Kunden in die Datenbank Orderhistory ein.
    *
    * @param orderID         Bestellnummer für die Bestellung
    * @param customerID      Kundennummer
    * @param customerData    Kundendaten
    * @param orderedProducts bestellte Produkte
    * @param sumOfOrder      Gesamtsummer der Bestellung
    * @param orderDate       Datum der Bestellung
    */
  def addToHistory(orderID: Long, customerID: Long, customerData: String, orderedProducts: String, sumOfOrder: Double,
                   orderDate: String): OrderHistory = {
    // create User
    val newHistory = OrderHistory(orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate)
    // persist and return User
    orderHistoryDao.addToHistory(newHistory)
  }

  /**
    * Entfernt eine Bestellung aus der Datenbank Orderhistory.
    *
    * @param id id der Bestellung
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromHistory(id: Long): Boolean = orderHistoryDao.rmFromHistory(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */

  def showOrdersUser(id: Long): List[OrderHistory] = {
    orderHistoryDao.showOrdersUser(id)
  }

  def showOrdersEmployee: List[OrderHistory] = {
    orderHistoryDao.showOrdersEmployee
  }
}

object OrderHistoryService extends OrderHistoryServiceT
