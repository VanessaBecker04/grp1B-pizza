package services

import dbaccess.{MenuDao, MenuDaoT}
import models.Menu

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  * Service Klasse für Speisekarte (Menu) bezogene Handlungen.
  */

trait MenuServiceT {

  val menuDao: MenuDaoT = MenuDao

  /**
    * Fügt ein neues Produkt in die Datenbank Menu.
    *
    * @param name Name des neuen Produktes
    * @param price Preis/Unit für das neue Produkt
    * @param category Kategorie des neuen Produktes
    * @return das neue Produkte
    */
  def addToMenu(name: String, price: Double, category: String): Menu = {
    // create User
    val ordered: Boolean = false
    val active: Boolean = true
    val newMenu = Menu(-1, name, price, category, ordered, active)
    // persist and return User
    menuDao.addToMenu(newMenu)
  }

  /** Verändert einzelen Attribute eines Produktes in der Datenbank.
    *
    * @param id id des Produktes was sich in der Datenbank befindet
    * @param name neuer Name für das bestehende Produkt
    * @param price neuer Preis für das bestehende Produkt
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

}

object MenuService extends MenuServiceT

