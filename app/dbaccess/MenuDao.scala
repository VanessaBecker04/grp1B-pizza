package dbaccess

import anorm.NamedParameter.symbol
import anorm.SQL
import anorm.SqlParser.scalar
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
      val first = SQL("Select count(id) from Menu where category = {category}").on('category -> menu.category).as(scalar[Long].singleOpt)
      if (first.isDefined) {
        SQL("Delete from Menu where name='' and category = {category}").on('category -> menu.category).executeUpdate()
      }
      val id: Option[Long] =
        SQL("insert into Menu(name, price, unit, category, ordered, active) values ({name}, {price}, {unit}, {category}, {ordered}, {active})").on(
          'name -> menu.name, 'price -> menu.price, 'unit -> menu.unit, 'category -> menu.category, 'ordered -> menu.ordered, 'active -> menu.active).executeInsert()
      menu.id = id.get
      menu
    }
  }

  def addCategoryToMenu(menu: Menu): Menu = {
    DB.withConnection { implicit c =>
      val exists = SQL("select distinct category from Menu where category = {category} and active = false;").on('category -> menu.category).as(scalar[String].singleOpt)
      if (exists.isDefined) {
        SQL("Update Menu set active = true where category = {category};").on('category -> menu.category).executeUpdate()
      } else {
        addToMenu(menu)
      }
      menu
    }
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
      val rowsCount = SQL("Delete from Menu where id = {id} and ordered=false").on('id -> id).executeUpdate()
      SQL("Update Menu set active=false where id = {id};").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  def rmCategory(category: String): Boolean = {
    DB.withConnection { implicit c =>
      val remove = SQL("Delete from Menu where category = {category} and ordered=false;").on('category -> category).executeUpdate()
      SQL("Update Menu set active=false where category = {category};").on('category -> category).executeUpdate()
      remove > 0
    }
  }

  def editCategory(oldCategory: String, newCategory: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set category={newCategory} where category = {oldCategory}").on('oldCategory -> oldCategory, 'newCategory -> newCategory).executeUpdate()
    }
  }

  def listOfProducts: List[Menu] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select * from Menu;")
      // Transform the resulting Stream[Row] to a List[(Menu,Menu)]
      selectFromMenu().map(row => Menu(row[Long]("id"), row[String]("name"),
        row[Double]("price"), row[String]("unit"), row[String]("category"), row[Boolean]("ordered"), row[Boolean]("active"))).toList
    }
  }

  /** Setzt das Produkt als einmal bestellt.
    *
    */
  def setProductOrdered(products: List[Long]): Unit = {
    DB.withConnection { implicit c =>
      for (id <- products)
      SQL("Update Menu set ordered=true where id = {id}").on('id -> id).executeUpdate()
    }
  }
}

object MenuDao extends MenuDaoT