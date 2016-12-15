package services

import java.util.Date

import dbaccess.{OrderHistoryDao, OrderHistoryDaoT}
import models.OrderHistory

/**
  * Created by Hasi on 14.12.2016.
  */

trait OrderHistoryServiceT {

  val orderHistoryDao: OrderHistoryDaoT = OrderHistoryDao

  /**
    * Adds a new user to the system.
    *
    * @param name name of the new user.
    * @return the new user.
    */
  def addToHistory(orderID: Long, customerID: Long, customerData: String, orderedProducts: String, sumOfOrder: Double,
                   orderDate: Date): OrderHistory = {
    // create User
    val newHistory = OrderHistory(orderID, customerID, customerData, orderedProducts, sumOfOrder, orderDate)
    // persist and return User
    orderHistoryDao.addToHistory(newHistory)
  }

  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
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
