package dbaccess

import anorm._
import models.OrderHistory
import play.api.Play.current
import play.api.db.DB

/**
  * Created by Hasibullah Faroq und Maximilian Öttl on 14.12.2016.
  * Datenbankzugriff über Benutzerschnittstellen für die kompletten Bestellunge Datenbank (Orderhistory)
  */

trait OrderHistoryDaoT {
  /**
    * fügt ein neue Bestellung des Kunden in die Datenbank Orderhistory ein.
    *
    * @param coh das Orderhistory Objekt was in die Datenbank gespeichert werden soll.
    * @return die Orderhistory Objekt
    */
  def addToHistory(coh: OrderHistory): OrderHistory = {
    DB.withConnection { implicit c =>
      val orderID: Option[Long] =
        SQL("insert into Orderhistory(customerID, customerData, orderedProducts, sumOfOrder, orderDate) values ({customerId}, {customerData}, {orderedProducts},{sumOfOrder}, {orderDate})").on(
          'customerId -> coh.customerID, 'customerData -> coh.customerData, 'orderedProducts -> coh.orderedProducts, 'sumOfOrder -> coh.sumOfOrder, 'orderDate -> coh.orderDate).executeInsert()
      coh.orderID = orderID.get
    }
    coh
  }

  /**
    * Entfernt eine Bestellung aus der Datenbank Orderhistory.
    *
    * @param id id der Bestellung
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromHistory(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Menu where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of all orders from the database.
    *
    * @return a list of order objects
    */
  def showOrdersEmployee: List[OrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate from Orderhistory")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("orderID"), row[Long]("customerID"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[String]("orderDate"))).toList
      history
    }
  }

  /**
    * Returns a list of all orders of a specified user from the database.
    *
    * @return a list of order objects
    */
  def showOrdersUser(id: Long): List[OrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate from Orderhistory where customerId = {id}").on('id -> id)
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("orderID"), row[Long]("customerID"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[String]("orderDate"))).toList
      history
    }
  }
}

object OrderHistoryDao extends OrderHistoryDaoT
