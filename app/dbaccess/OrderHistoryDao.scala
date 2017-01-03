package dbaccess

import java.util.Date

import anorm._
import models.OrderHistory
import play.api.Play.current
import play.api.db.DB

/**
  * Created by Hasi on 14.12.2016.
  */

trait OrderHistoryDaoT {
  /**
    * Creates the given order in the database.
    *
    * @param coh the order object to be stored.
    * @return the persisted order object
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
    * Removes a menu item by id from the database.
    *
    * @param id the menu item id
    * @return a boolean success flag
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
