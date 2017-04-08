package dbaccess

import anorm.NamedParameter.symbol
import anorm.{SQL, _}
import models.Bill
import play.api.Play.current
import play.api.db.DB

/** Datenbankzugriff über Benutzerschnittstellen für die Rechnungserstellung (Orderbill)
  * Created by Hasi on 28.11.2016.
  */

trait OrderDaoT {
  /**
    * Fügt eine neue Rechnung zu der Datenbank Orderbill
    *
    * @param bill das Bill Objekt was in der Datenbank gespeichert werden soll.
    * @return das Bill Objekt
    */
  def addToOrder(bill: Bill): Bill = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Orderbill(customerID, pizzaName, pizzaNumber, pizzaSize, beverageName, beverageNumber, beverageSize, dessertName, dessertNumber) values ({customerId},{pizzaName},{pizzaNumber},{pizzaSize},{beverageName},{beverageNumber},{beverageSize},{dessertName},{dessertNumber})").on(
          'customerId -> bill.customerID, 'pizzaName -> bill.pizzaName, 'pizzaNumber -> bill.pizzaNumber, 'pizzaSize -> bill.pizzaSize, 'beverageName -> bill.beverageName, 'beverageNumber -> bill.beverageNumber, 'beverageSize -> bill.beverageSize, 'dessertName -> bill.dessertName, 'dessertNumber -> bill.dessertNumber).executeInsert()
      bill.id = id.get
    }
    bill
  }

  /**
    * entfernt die Rechnungs Daten von der Datenbank Orderbill für id
    *
    * @param id Oderbill id
    * @return wahrheitswert ob die Löschung erfolgreich war
    */
  def rmFromOrder(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Orderbill where id = {id}").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Gibt eine Liste zurück mit allen vorhandenen Rechnungen
    *
    * @return eine liste von Bill objekten.
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

  /**
    * Entfernt Rechnung, wenn Bestellung abgebrochen wurde.
    */
  def cancelOrder() {
    DB.withConnection { implicit c =>
      SQL("Delete from Orderbill where id " +
        "not in (select orderID from Orderhistory)").executeUpdate()
    }
  }
}

object OrderDao extends OrderDaoT
