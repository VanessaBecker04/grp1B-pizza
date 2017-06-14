package dbaccess

import anorm.NamedParameter.symbol
import anorm.SQL
import anorm.SqlParser.scalar
import models.Menu
import play.api.Play.current
import play.api.db.DB

/** Database access via user interfaces for the menus Database.
  *
  * @author Maximilian Öttl, Hasibullah Faroq
  */

trait MenuDaoT {

  /** Adds a new product to the menu.
    *
    * @param menu menu object
    * @return menu object
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

  /** Adds a new category to the menu.
    *
    * @param menu object menu
    * @return menu
    */
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

  /** Changes information about products in the database.
    *
    * @param id     id of product database
    * @param name   new name fot the existing product
    * @param price  new price for the existing product
    * @param active new status for the existing product
    */
  def updateInMenu(id: Long, name: String, price: Double, active: Boolean): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set name={name}, price={price}, active={active} where id = {id}").on('id -> id, 'name -> name, 'price -> price, 'active -> active).executeUpdate()
    }
  }

  /** Deletes a product from the database.
    *
    * @param id id of the product
    * @return  success of deletion
    */
  def rmFromMenu(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("Delete from Menu where id = {id} and ordered=false").on('id -> id).executeUpdate()
      SQL("Update Menu set active=false where id = {id};").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /** Deletes a category from the database.
    *
    * @param category category
    * @return success of the deletion
    */
  def rmCategory(category: String): Boolean = {
    DB.withConnection { implicit c =>
      val remove = SQL("Delete from Menu where category = {category} and ordered=false;").on('category -> category).executeUpdate()
      SQL("Update Menu set active=false where category = {category};").on('category -> category).executeUpdate()
      remove > 0
    }
  }

  /** Changes the name of a category.
    *
    * @param oldCategory current name of category
    * @param newCategory new name of category
    */
  def editCategory(oldCategory: String, newCategory: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("Update Menu set category={newCategory} where category = {oldCategory}").on('oldCategory -> oldCategory, 'newCategory -> newCategory).executeUpdate()
    }
  }

  /** Returns a list of all product.
    *
    * @return list of products
    */
  def listOfProducts: List[Menu] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select id, name, price, unit, category, ordered, active from Menu;")
      // Transform the resulting Stream[Row] to a List[(Menu,Menu)]
      selectFromMenu().map(row => Menu(row[Long]("id"), row[String]("name"),
        row[Double]("price"), row[String]("unit"), row[String]("category"), row[Boolean]("ordered"), row[Boolean]("active"))).toList
    }
  }

  /** Sets the product status to ordered */
  def setProductOrdered(products: List[Long]): Unit = {
    DB.withConnection { implicit c =>
      for (id <- products)
      SQL("Update Menu set ordered=true where id = {id}").on('id -> id).executeUpdate()
    }
  }
}

object MenuDao extends MenuDaoT