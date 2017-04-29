package services

import dbaccess.{MenuDao, MenuDaoT}
import models.{CategoryPlusUnit, Menu}

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  * Service Klasse für Speisekarte (Menu) bezogene Handlungen.
  */

trait MenuServiceT {

  /** Objekt, welches vier Listen verwaltet.
    * Jede Liste beeinhaltet Produkte die sich innerhalb der Menu Datenbank befinden.
    * Jede Liste ist nach einer bestimmten Kategorie befüllt (Pizza, Getränk, Dessert)
    *
    */
  var allIdFromMenu: List[Long] = _
  val menuDao: MenuDaoT = MenuDao

  /**
    * Fügt ein neues Produkt in die Datenbank Menu.
    *
    * @param name     Name des neuen Produktes
    * @param price    Preis/Unit für das neue Produkt
    * @param category Kategorie des neuen Produktes
    * @return das neue Produkte
    */
  def addToMenu(name: String, price: Double, unitOfMeasurement: String, category: String): Menu = {
    // create User
    val ordered: Boolean = false
    val active: Boolean = true
    val newMenu = Menu(-1, name, price, unitOfMeasurement, category, ordered, active)
    // persist and return User
    menuDao.addToMenu(newMenu)
  }

  /** Verändert einzelen Attribute eines Produktes in der Datenbank.
    *
    * @param id     id des Produktes was sich in der Datenbank befindet
    * @param name   neuer Name für das bestehende Produkt
    * @param price  neuer Preis für das bestehende Produkt
    * @param active neuer Status für das Produkt
    */
  def updateInMenu(id: Long, name: String, price: Double, active: Boolean): Unit = {
    menuDao.updateInMenu(id, name, price, active)
  }

  def updateCategory(oldCategory: String, newCategory: String): Unit = {
    menuDao.updateCategory(oldCategory, newCategory)
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
  def addedToMenu: List[Menu] = {
    menuDao.addedToMenu
  }

  def listCategories: List[String] = {
    menuDao.listCategories
  }

  /** Setzt das Produkt als bestellt.
    *
    */
  def setProductOrdered(products: List[Long]): Unit = {
    menuDao.setProductOrdered(products)
  }

  /** Setzt das Produkt Inaktiv, damit sie nicht weiterhin bestellt werden kann.
    *
    * @param id die id des Produktes welches inaktiv gestellt werden
    */
  def setProductInactive(id: Long): Unit = menuDao.setProductInactive(id)

  /** befüllt die Liste mit allen Produkt-IDs
    *
    */
  def putAllMenuIDInList() {
    val allID = new ListBuffer[Long]
    for (s <- services.MenuService.addedToMenu) {
      allID += s.id
    }
    MenuService.allIdFromMenu = allID.toList
  }

  def listCategoriesPlusUnit: List[CategoryPlusUnit] = menuDao.listCategoriesPlusUnit
}

object MenuService extends MenuServiceT