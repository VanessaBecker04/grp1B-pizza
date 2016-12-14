package dbaccess

import java.util.Date

import anorm._
import models.{CustomerOrderHistory, Menu}
import play.api.db.DB
import play.api.Play.current

/**
  * Created by Hasi on 14.12.2016.
  */

trait CustomerOrderHistoryDaoT {
  /**
    * Creates the given user in the database.
    *
    * @param menu the user object to be stored.
    * @return the persisted user object
    */
  def addToHistory(coh: CustomerOrderHistory): CustomerOrderHistory = {
    DB.withConnection { implicit c =>
        SQL("insert into Customerorderhistory(customerId, customerData, orderedProducts, sumOfOrder, orderDate) " +
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
  def addedToHistory: List[CustomerOrderHistory] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select customerId, customerData, orderedProducts, sumOfOrder, orderDate from Customerorderhistory;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val history = selectFromMenu().map(row => CustomerOrderHistory(row[Long]("customerId"), row[String]("customerData"),
        row[String]("orderedProducts"), row[Double]("sumOfOrder"), row[Date]("orderDate"))).toList
      history
    }
  }
}
object CustomerOrderHistoryDao extends CustomerOrderHistoryDaoT