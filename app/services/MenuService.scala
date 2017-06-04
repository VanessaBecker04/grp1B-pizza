package services

import dbaccess.{MenuDao, MenuDaoT}
import models.Menu

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  * Service Klasse für Speisekarte (Menu) bezogene Handlungen.
  */

trait MenuServiceT {

  val menuDao: MenuDaoT = MenuDao

  /**
    * Fügt ein neues Produkt in die Datenbank Menu.
    *
    * @param name     Name des neuen Produktes
    * @param price    Preis/Unit für das neue Produkt
    * @param category Kategorie des neuen Produktes
    * @return das neue Produkte
    */
  def addToMenu(name: String, price: Double, unit: String, category: String): Menu = {
    menuDao.addToMenu(Menu(-1, name, price, unit, category, false, true))
  }

  def addCategory(name: String, unit: String): Menu = {
      menuDao.addCategoryToMenu(Menu(-1, "", 0, unit, name, false, true))
  }

  /**
    * Verändert einzelen Attribute eines Produktes in der Datenbank.
    *
    * @param id     id des Produktes was sich in der Datenbank befindet
    * @param name   neuer Name für das bestehende Produkt
    * @param price  neuer Preis für das bestehende Produkt
    * @param active neuer Status für das Produkt
    */
  def updateInMenu(id: Long, name: String, price: Double, active: Boolean): Unit = {
    menuDao.updateInMenu(id, name, price, active)
  }

  def editCategory(oldCategory: String, newCategory: String): Unit = {
    menuDao.editCategory(oldCategory, newCategory)
  }

  /**
    * Entfernt ein Produkt aus der Datenbank.
    *
    * @param id id des Produktes
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromMenu(id: Long): Boolean = menuDao.rmFromMenu(id)

  def rmCategory(category: String): Boolean = menuDao.rmCategory(category)

  /**
    * Gibt eine Liste zurück mit allen vorhandenen Produkten.
    *
    * @return eine Liste von Produkten
    */

  def listOfAllProducts: List[Menu] = {
    menuDao.listOfProducts
  }

  def listOfEditableProducts: List[Menu] = {
    var products = new ListBuffer[Menu]
    for (p <- listOfAllProducts)
      if (!p.name.equals("")) products += p
    products.toList
  }

  def listOfOrderableProducts: List[Menu] = {
    var products = new ListBuffer[Menu]
    for (p <- listOfEditableProducts)
      if (p.active) products += p
    products.toList
  }

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

  def listOfAddableCategories: List[Menu] = {
    var categories = new ListBuffer[Menu]
    for (p <- listOfAllCategories)
      if (p.active) categories += p
    categories.toList
  }

  def listOfOrderableCategories: List[Menu] = {
    var categories = new ListBuffer[Menu]
    for (p <- listOfAllCategories)
      if (!p.name.equals("") && p.active) categories += p
    categories.toList
  }

  /**
    * Setzt das Produkt als bestellt.
    *
    */
  def setProductOrdered(products: List[Long]): Unit = {
    menuDao.setProductOrdered(products)
  }
}

object MenuService extends MenuServiceT