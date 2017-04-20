package services

import dbaccess.{MenuDao, MenuDaoT}
import models.Menu

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
  var pizzaList: List[String] = _
  var beverageList: List[String] = _
  var dessertList: List[String] = _
  var allIdFromMenu: List[Long] = _

  var pizza: String = _
  var beverage: String = _
  var dessert: String = _

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

  /**
    * Entfernt ein Produkt aus der Datenbank.
    *
    * @param id id des Produktes
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromMenu(id: Long): Boolean = menuDao.rmFromMenu(id)

  /**
    * Gibt eine Liste zurück mit allen vorhandenen Produkten.
    *
    * @return eine Liste von Produkten
    */
  def addedToMenu: List[Menu] = {
    menuDao.addedToMenu
  }

  /** Setzt das Produkt als einmal bestellt.
    *
    * @param id die Id des bestellten Produktes
    */
  def setProductOrdered(id: Long): Unit = menuDao.setProductOrdered(id)

  /** Setzt das Produkt Inaktiv, damit sie nicht weiterhin bestellt werden kann.
    *
    * @param id die id des Produktes welches inaktiv gestellt werden
    */
  def setProductInactive(id: Long): Unit = menuDao.setProductInactive(id)

  def categorize() {

    val pizzaList = new ListBuffer[String]
    val beverageList = new ListBuffer[String]
    val dessertList = new ListBuffer[String]

    val menuList = services.MenuService.addedToMenu
    for (m <- menuList) {
      if (m.category.equals("Pizza") && m.active) {
        pizzaList += m.name
      }
      if (m.category.equals("Getränk") && m.active) {
        beverageList += m.name
      }
      if (m.category.equals("Dessert") && m.active) {
        dessertList += m.name
      }
    }

    MenuService.pizzaList = pizzaList.toList
    MenuService.beverageList = beverageList.toList
    MenuService.dessertList = dessertList.toList

  }

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

  /** übergibt Werte an das Objekt UndeleteableProducts
    *
    * @param pizza      Name der bestellten Pizza
    * @param pizzaNr    Anzahl der bestellten Pizza
    * @param beverage   Name der bestellten Produkte
    * @param beverageNr Anzahl der bestellten Getränke
    * @param dessert    Name des bestellten Desserts
    * @param dessertNr  Anzahl der bestellten Desserts
    *
    */
  def setUndeleteable(pizza: String, pizzaNr: Int, beverage: String, beverageNr: Int, dessert: String, dessertNr: Int) {

    if (pizzaNr != 0) {
      MenuService.pizza = pizza
    }
    if (beverageNr != 0) {
      MenuService.beverage = beverage
    }
    if (dessertNr != 0) {
      MenuService.dessert = dessert
    }
  }

}

object MenuService extends MenuServiceT

