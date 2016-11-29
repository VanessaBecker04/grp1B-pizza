package services

import dbaccess.{OrderDao, OrderDaoT}
import models.Bill

/**
  * Created by Hasi on 28.11.2016.
  */

trait OrderServiceT {
  val orderDao: OrderDaoT = OrderDao

  /**
    * Adds a new user to the system.
    *
    * @return the new user.
    */
  def addToOrder(customerId: Long, pizzaName: String, pizzaNumber: Int, pizzaSize: String, beverageName: String,
                 beverageNumber: Int, beverageSize: String, dessertName: String, dessertNumber: Int): Bill = {
    // create User
    val newOrder = Bill(-1, customerId, pizzaName, pizzaNumber, pizzaSize, beverageName,
      beverageNumber, beverageSize, dessertName, dessertNumber)
    // persist and return User
    orderDao.addToOrder(newOrder)
  }

  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
    */
  def rmFromOrder(id: Long): Boolean = orderDao.rmFromOrder(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def addedToOrder: List[Bill] = {
    orderDao.addedToOrder
  }
}
object OrderService extends OrderServiceT
