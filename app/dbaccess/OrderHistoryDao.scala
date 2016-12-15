package dbaccess

import java.util.Date

import anorm._
import models.OrderHistory
import play.api.db.DB
import play.api.Play.current

/**
  * Created by Hasi on 14.12.2016.
  */

trait OrderHistoryDaoT {
  /**
    * Creates the given user in the database.
    *
    * @param menu the user object to be stored.
    * @return the persisted user object
    */
  def addToHistory(coh: OrderHistory): OrderHistory = {
    DB.withConnection { implicit c =>
        SQL("insert into Orderhistory(customerId, customerData, orderedProducts, sumOfOrder, orderDate) " +
          "values ({customerId}, {customerData}, {orderedProducts},{sumOfOrder}, {orderDate})").on(
          'customerId -> coh.customerId, 'customerData -> coh.customerData,
          'orderedProducts -> coh.orderedProducts, 'sumOfOrder -> coh.sumOfOrder, 'orderDate -> coh.orderDate).executeInsert()
    }
    coh
  }

  /**
    * Removes a user by id from the database.
    *
    * @param id the users id
    * @return a boolean success flag
    */
  def rmFromHistory(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Menu where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of available user from the database.
    *
    * @return a list of user objects.
    */
  def showOrdersEmployee: List[OrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select customerId, customerData, orderedProducts, sumOfOrder, orderDate from Orderhistory")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("customerId"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[Date]("orderDate"))).toList
      history
    }
  }
  def showOrdersUser(id: Long): List[OrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select customerId, customerData, orderedProducts, sumOfOrder, orderDate from Orderhistory where customerId = {id}").on('id -> id)
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => OrderHistory(row[Long]("customerId"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[Date]("orderDate"))).toList
      history
    }
  }
}
object OrderHistoryDao extends OrderHistoryDaoT
