package services

import dbaccess.{MenuDao, MenuDaoT}
import models.Menu

/**
  * Created by Hasi on 21.11.2016.
  */

trait MenuServiceT {

  val menuDao: MenuDaoT = MenuDao

  /**
    * Adds a new user to the system.
    *
    * @param name name of the new user.
    * @return the new user.
    */
  def addToMenu(name: String, price: Int, category: String): Menu = {
    // create User
    val newMenu = Menu(-1, name, price, category)
    // persist and return User
    menuDao.addToMenu(newMenu)
  }

  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
    */
  def rmFromMenu(id: Long): Boolean = menuDao.rmFromMenu(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def addedToMenu: List[Menu] = {
    menuDao.addedToMenu
  }
}

object MenuService extends MenuServiceT

