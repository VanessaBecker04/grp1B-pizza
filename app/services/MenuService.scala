package services

import dbaccess.{MenuDao, MenuDaoT}
import models.Menu

import scala.collection.mutable.ListBuffer

/**
  * Class for service to act on menu.
  *
  * @author Hasibullah Faroq.
  */
trait MenuServiceT {

  val menuDao: MenuDaoT = MenuDao

  /**
    * Adds a new product to the database menu.
    *
    * @param name     Name of the new product.
    * @param price    Price per unit of the new product.
    * @param unit     Unit of the new product.
    * @param category Category of the new product.
    * @return         New product.
    */
  def addToMenu(name: String, price: Double, unit: String, category: String): Menu = {
    menuDao.addToMenu(Menu(-1, name, price, unit, category, false, true))
  }
  /**
    * Adds a new category to the database menu.
    *
    * @param name     Name of the new category.
    * @param unit     Unit of the new category.
    * @return         New category.
    */
  def addCategory(name: String, unit: String): Menu = {
      menuDao.addCategoryToMenu(Menu(-1, "", 0, unit, name, false, true))
  }

  /**
    * Modifies individual attributes of the product in the database.
    *
    * @param id     ID of the Product which is saved in the database.
    * @param name   New name for the existing product.
    * @param price  New price for the existing product.
    * @param active New status for the product.
    */
  def updateInMenu(id: Long, name: String, price: Double, active: Boolean): Unit = {
    menuDao.updateInMenu(id, name, price, active)
  }

  /**
    * Modifies individual attributes of the category in the database.
    *
    * @param oldCategory   Old category.
    * @param newCategory   New category.
    */
  def editCategory(oldCategory: String, newCategory: String): Unit = {
    menuDao.editCategory(oldCategory, newCategory)
  }

  /**
    * Remove a product of the database menu.
    *
    * @param id Number of the product.
    * @return   True worth whether the deletion was successful.
    */
  def rmFromMenu(id: Long): Boolean = menuDao.rmFromMenu(id)

  /**
    * Remove a category of the database menu.
    *
    * @param category Name of the category.
    * @return         True worth whether the deletion was successful.
    */
  def rmCategory(category: String): Boolean = menuDao.rmCategory(category)

  /**
    * Returns a list of all existing products.
    *
    * @return List of products.
    */
  def listOfAllProducts: List[Menu] = {
    menuDao.listOfProducts
  }

  /**
    * Returns a list of editable products.
    *
    * @return List of etitable products.
    */
  def listOfEditableProducts: List[Menu] = {
    var products = new ListBuffer[Menu]
    for (p <- listOfAllProducts)
      if (!p.name.equals("")) products += p
    products.toList
  }

  /**
    * Returns a list of orderable products.
    *
    * @return List of orderable products.
    */
  def listOfOrderableProducts: List[Menu] = {
    var products = new ListBuffer[Menu]
    for (p <- listOfEditableProducts)
      if (p.active) products += p
    products.toList
  }

  /**
    * Returns a list of all categories.
    *
    * @return List of all categories.
    */
  def listOfAllCategories: List[Menu] = {
    var categories = new ListBuffer[Menu]
    for (p <- listOfAllProducts) {
      if (p.active)
        if (!categories.toList.exists(c => c.category == p.category)) categories += p
    }
    for (p <- listOfAllProducts) {
      if (!categories.toList.exists(c => c.category == p.category)) categories += p
    }
    categories.toList
  }

  /**
    * Returns a list of addable categories.
    *
    * @return List of addable categories.
    */
  def listOfAddableCategories: List[Menu] = {
    var categories = new ListBuffer[Menu]
    for (p <- listOfAllCategories)
      if (p.active) categories += p
    categories.toList
  }

  /**
    * Returns a list of orderable categories.
    *
    * @return List of orderable categories.
    */
  def listOfOrderableCategories: List[Menu] = {
    var categories = new ListBuffer[Menu]
    for (p <- listOfAllCategories)
      if (!p.name.equals("") && p.active) categories += p
    categories.toList
  }

  /**
    * Marks the product as ordered.
    *
    * @param products Products which were ordered.
    */
  def setProductOrdered(products: List[Long]): Unit = {
    menuDao.setProductOrdered(products)
  }
}

object MenuService extends MenuServiceT