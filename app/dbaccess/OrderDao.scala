package dbaccess

import anorm.SQL
import models.OrderHistory
import play.api.Play.current
import play.api.db.DB

/**
  * Database access via user interfaces for the order Database (Orderhistory).
  *
  * Created by Hasibullah Faroq and Maximilian Öttl on 14.12.2016.
  */

trait OrderDaoT {

  /**
    * Adds a new order of the customer to the database Orderhistory.
    *
    * @param coh Orderhistory object which is back uo in the database
    * @return Orderhistory object
    */
  def addToHistory(coh: OrderHistory): OrderHistory = {
    DB.withConnection { implicit c =>
      val orderID: Option[Long] =
        SQL("insert into Orderhistory(customerID, customerData, orderedProducts, sumOfOrder, orderDate, status) values ({customerId}, {customerData}, {orderedProducts},{sumOfOrder}, {orderDate}, {status})").on(
          'customerId -> coh.customerID, 'customerData -> coh.customerData, 'orderedProducts -> coh.orderedProducts, 'sumOfOrder -> coh.sumOfOrder, 'orderDate -> coh.orderDate, 'status -> coh.status).executeInsert()
      coh.orderID = orderID.get
    }
    coh
  }

  /**
    * Deletes an order of the database OrderHistory.
    *
    * @param id id of order
    * @return success of deletion
    */
  def rmFromHistory(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from OrderHistory where orderID = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of all orders that were taken by employee.
    *
    * @return a list of order objects
    */
  def showOrdersEmployee: List[OrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate, status from Orderhistory")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("orderID"), row[Long]("customerID"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[String]("orderDate"), row[String]("status"))).toList
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
      val selectFromMenu = SQL("Select orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate, status from Orderhistory where customerId = {id}").on('id -> id)
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("orderID"), row[Long]("customerID"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[String]("orderDate"), row[String]("status"))).toList
      history
    }
  }

  /**
    * Sets the status for an order.
    *
    * @param id id of order
    * @param newStatus new Statuss of order
    */
  def setStatusForOrder(id: Long, newStatus: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update OrderHistory set status={newStatus} where orderID = {id}").on('newStatus -> newStatus, 'id -> id).executeUpdate()
    }
  }
}

object OrderDao extends OrderDaoT