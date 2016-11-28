package dbaccess

import anorm.NamedParameter.symbol
import anorm.SQL
import models.Menu
import play.api.Play.current
import play.api.db.DB

/**
  * Created by Hasi on 21.11.2016.
  */

trait MenuDaoT {

  /**
    * Creates the given user in the database.
    *
    * @param menu the user object to be stored.
    * @return the persisted user object
    */
  def addToMenu(menu: Menu): Menu = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Menu(name, price, category) values ({name}, {price}, {category})").on(
          'name -> menu.name, 'price -> menu.price, 'category -> menu.category).executeInsert()
      menu.id = id.get
    }
    menu
  }

  /**
    * Removes a user by id from the database.
    *
    * @param id the users id
    * @return a boolean success flag
    */
  def rmFromMenu(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Menu where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of available user from the database.
    *
    * @return a list of user objects.
    */
  def addedToMenu: List[Menu] = {
    DB.withConnection { implicit c =>
      val selectFromMenu = SQL("Select id, name, price, category from Menu;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val products = selectFromMenu().map(row => Menu(row[Long]("id"), row[String]("name"),
        row[Int]("price"), row[String]("category"))).toList
      products
    }
  }

}

object MenuDao extends MenuDaoT

