package services

import dbaccess.{OrderDao, OrderDaoT}
import models.{Bill, activeUser, setOrder}

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
  def addToOrder(customerID: Long, pizzaName: String, pizzaNumber: Int, pizzaSize: String, beverageName: String,
                 beverageNumber: Int, beverageSize: String, dessertName: String, dessertNumber: Int): Bill = {
    // create User
    val newOrder = Bill(-1, customerID, pizzaName, pizzaNumber, pizzaSize, beverageName,
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

  def doCalculationForBill(): Unit = {

    val bill = addedToOrder
    val menu = services.MenuService.addedToMenu

    var pizzaSum: Double = 0
    var beverageSum: Double = 0
    var dessertSum: Double = 0
    var wholeSum: Double = 0
    var orderedProducts: StringBuilder = new StringBuilder


    for (c <- bill) {
      if (c.customerID.equals(models.activeUser.id) && c.id.equals(models.activeUser.orderID)) {
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
    models.setOrder(models.activeUser.orderID, models.activeUser.id, orderedProducts, wholeSum)
  }

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def addedToOrder: List[Bill] = {
    orderDao.addedToOrder
  }

  def cancelOrder() = {
    orderDao.cancelOrder()
  }
}

object OrderService extends OrderServiceT
