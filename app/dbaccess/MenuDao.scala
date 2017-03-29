package dbaccess

import anorm.NamedParameter.symbol
import anorm.SQL
import models.Menu
import play.api.Play.current
import play.api.db.DB

/** Datenbankzugriff über Benutzerschnittstellen für die Speisekarten Datenbank (MENU)
  * Created by Hasibullah Faroq on 21.11.2016.
  */

trait MenuDaoT {

  /**
    * fügt ein neues Produkt in die Datenbank Menu ein.
    *
    * @param menu das Menu Objekt was in die Datenbank gespeichert werden soll.
    * @return das Menu Objekt
    */
  def addToMenu(menu: Menu): Menu = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Menu(name, price, category, ordered, active) values ({name}, {price}, {category}, {ordered}, {active})").on(
          'name -> menu.name, 'price -> menu.price, 'category -> menu.category, 'ordered -> menu.ordered, 'active -> menu.active).executeInsert()
      menu.id = id.get
    }
    menu
  }

  /** Verändert einzelen Attribute eines Produktes in der Datenbank.
    *
    * @param id     id des Produktes was sich in der Datenbank befindet
    * @param name   neuer Name für das bestehende Produkt
    * @param price  neuer Preis für das bestehende Produkt
    * @param active neuer Status für das Produkt
    */
  def updateInMenu(id: Long, name: String, price: Double, active: Boolean): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set name={name}, price={price}, active={active} where id = {id}").on('id -> id, 'name -> name, 'price -> price, 'active -> active).executeUpdate()
    }
  }

  /**
    * Entfernt ein Produkt aus der Datenbank.
    *
    * @param id id des Produktes
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromMenu(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Menu where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Gibt eine Liste zurück mit allen vorhandenen Produkten.
    *
    * @return eine Liste von Produkten
    */
  def addedToMenu: List[Menu] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select id, name, price, category, ordered, active from Menu;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val products = selectFromMenu().map(row => Menu(row[Long]("id"), row[String]("name"),
        row[Double]("price"), row[String]("category"), row[Boolean]("ordered"), row[Boolean]("active"))).toList
      products
    }
  }

  /** Setzt das Produkt als einmal bestellt.
    *
    * @param id die Id des bestellten Produktes
    */
  def setProductOrdered(id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set ordered=true where id = {id}").on('id -> id).executeUpdate()
    }
  }

  /** Setzt das Produkt Inaktiv, damit sie nicht weiterhin bestellt werden kann.
    *
    * @param id die id des Produktes welches inaktiv gestellt werden
    */
  def setProductInactive(id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set active=false where id = {id}").on('id -> id).executeUpdate()
    }
  }

}

object MenuDao extends MenuDaoT

