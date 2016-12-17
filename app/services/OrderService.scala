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


  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def addedToOrder: List[Bill] = {
    orderDao.addedToOrder
  }

  def doCalculationForBill(): Unit = {

    val bill = addedToOrder
    val menu = services.MenuService.addedToMenu

    var pizzaSum: Double = 0
    var beverageSum: Double = 0
    var dessertSum: Double = 0
    var wholeSum: Double = 0
    var orderedProducts: StringBuilder = new StringBuilder


    for(c <- bill) {
      if(c.customerID.equals(models.activeUser.id) && c.id.equals(models.activeUser.orderID)) {
        if(c.pizzaName != null) {
          orderedProducts.append(c.pizzaNumber + "x ")
          orderedProducts.append(c.pizzaName + " ")
          orderedProducts.append("(" + c.pizzaSize + "), ")
          for(m <- menu) {
            if(m.name.equals(c.pizzaName)) {
              if(c.pizzaSize.equals("medium")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 27) * c.pizzaNumber
              }
              if(c.pizzaSize.equals("large")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 32) * c.pizzaNumber
              }
              if(c.pizzaSize.equals("xl")) {
                pizzaSum += m.price
                pizzaSum = (pizzaSum * 36) * c.pizzaNumber
              }
            }
          }
        } else {
        }
        if(c.beverageName != null) {
          orderedProducts.append(c.beverageNumber + "x ")
          orderedProducts.append(c.beverageName)
          orderedProducts.append("(" + c.beverageSize + "), ")
          for(m <- menu) {
            if(m.name.equals(c.beverageName)) {
              if(c.beverageSize.equals("medium")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 5) * c.beverageNumber
              }
              if(c.beverageSize.equals("large")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 7.5) * c.beverageNumber
              }
              if(c.beverageSize.equals("xl")) {
                beverageSum += m.price
                beverageSum = (beverageSum * 10 ) * c.beverageNumber
              }
            }
          }
        } else {
        }
        if(c.dessertName != null) {
          orderedProducts.append(c.dessertNumber + "x ")
          orderedProducts.append(c.dessertName)
          for(m <- menu) {
            if(m.name.equals(c.dessertName)) {
              dessertSum += m.price
              dessertSum = dessertSum * c.dessertNumber
            }
          }
        } else {
        }
        wholeSum += (pizzaSum + beverageSum + dessertSum)
      }
    }
    models.setOrder(models.activeUser.orderID, models.activeUser.id, orderedProducts, wholeSum)
  }

  def cancelOrder() = {
    orderDao.cancelOrder()
    setOrder(0, 0, null, 0)
    activeUser.orderID = 0
  }
}
object OrderService extends OrderServiceT
