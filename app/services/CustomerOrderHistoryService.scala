package services

import java.util.Date

import dbaccess.{CustomerOrderHistoryDao, CustomerOrderHistoryDaoT, MenuDao, MenuDaoT}
import models.{CustomerOrderHistory, Menu}

/**
  * Created by Hasi on 14.12.2016.
  */

trait CustomerOrderHistoryServiceT {

  val customerOrderHistoryDao: CustomerOrderHistoryDaoT = CustomerOrderHistoryDao

  /**
    * Adds a new user to the system.
    *
    * @param name name of the new user.
    * @return the new user.
    */
  def addToHistory(customerId: Long, customerData: String, orderedProducts: String, sumOfOrder: Double,
                   orderDate: Date): CustomerOrderHistory = {
    // create User
    val newHistory = CustomerOrderHistory(customerId, customerData, orderedProducts, sumOfOrder, orderDate)
    // persist and return User
    customerOrderHistoryDao.addToHistory(newHistory)
  }

  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
    */
  def rmFromHistory(id: Long): Boolean = customerOrderHistoryDao.rmFromHistory(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def addedToHistory: List[CustomerOrderHistory] = {
    customerOrderHistoryDao.addedToHistory
  }
}

object CustomerOrderHistoryService extends CustomerOrderHistoryServiceT
