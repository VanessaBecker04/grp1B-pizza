package dbaccess

import anorm.NamedParameter.symbol
import anorm.{SQL, _}
import models.Bill
import play.api.Play.current
import play.api.db.DB

/**
  * Created by Hasi on 28.11.2016.
  */

trait OrderDaoT {
  /**
    * Creates the given user in the database.
    *
    * @param bill the user object to be stored.
    * @return the persisted user object
    */
  def addToOrder(bill: Bill): Bill = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Orderbill(customerID, pizzaName, pizzaNumber, pizzaSize, beverageName, beverageNumber, beverageSize, dessertName, dessertNumber) values ({customerId},{pizzaName},{pizzaNumber},{pizzaSize},{beverageName},{beverageNumber},{beverageSize},{dessertName},{dessertNumber})").on(
          'customerId -> bill.customerID, 'pizzaName -> bill.pizzaName, 'pizzaNumber -> bill.pizzaNumber, 'pizzaSize -> bill.pizzaSize, 'beverageName -> bill.beverageName, 'beverageNumber -> bill.beverageNumber, 'beverageSize -> bill.beverageSize, 'dessertName -> bill.dessertName, 'dessertNumber -> bill.dessertNumber).executeInsert()
      bill.id = id.get
      models.activeUser.orderID = id.get
    }
    bill
  }

  /**
    * Removes a user by id from the database.
    *
    * @param id the users id
    * @return a boolean success flag
    */
  def rmFromOrder(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Orderbill where id = {id}").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of available user from the database.
    *
    * @return a list of user objects.
    */
  def addedToOrder: List[Bill] = {
    DB.withConnection { implicit c =>
      val selectFromBill = SQL("Select id, customerId, pizzaName, pizzaNumber, pizzaSize," +
        " beverageName, beverageNumber, beverageSize, dessertName, dessertNumber from Orderbill;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val order = selectFromBill().map(row => Bill(row[Long]("id"), row[Long]("customerId"), row[String]("pizzaName"),
        row[Int]("pizzaNumber"), row[String]("pizzaSize"), row[String]("beverageName"),
        row[Int]("beverageNumber"), row[String]("beverageSize"), row[String]("dessertName"),
        row[Int]("dessertNumber"))).toList
      order
    }
  }

  def cancelOrder() {
    DB.withConnection { implicit c =>
      SQL("Delete from Orderbill where id " +
        "not in (select orderID from Orderhistory)").executeUpdate()
    }
  }
}

object OrderDao extends OrderDaoT
