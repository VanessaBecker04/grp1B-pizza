package models

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  */
case class Menu(var id: Long, var name: String, var price: Double, var category: String, var ordered: Boolean, var active: Boolean)

/** Objekt, welches vier Listen verwaltet.
  * Jede Liste beeinhaltet Produkte die sich innerhalb der Menu Datenbank befinden.
  * Jede Liste ist nach einer bestimmten Kategorie befüllt (Pizza, Getränk, Dessert)
  *
  */
object CategorizedMenu {

  var pizzaList: List[String] =_
  var beverageList: List[String] =_
  var dessertList: List[String] =_
  var allIdFromMenu: List[Long] =_
}

/** übergibt Werte an das Objekt CategorizedMenu
  *
  */
case class categorize() {

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

  CategorizedMenu.pizzaList = pizzaList.toList
  CategorizedMenu.beverageList = beverageList.toList
  CategorizedMenu.dessertList = dessertList.toList

}

/** befüllt die Liste mit allen Produkt-IDs
  *
  */
case class putAllMenuIDInList() {
  val allID = new ListBuffer[Long]
  for (s <- services.MenuService.addedToMenu) {
    allID += s.id
  }
  CategorizedMenu.allIdFromMenu = allID.toList
}

/** Objekt, welches Produkte verwaltet die nicht mehr gelöscht werden dürfen.
  *
  */
object UndeleteableProducts {
  var pizza: String =_
  var beverage: String =_
  var dessert: String =_
}

/** übergibt Werte an das Objekt UndeleteableProducts
  *
  * @param pizza Name der bestellten Pizza
  * @param pizzaNr Anzahl der bestellten Pizza
  * @param beverage Name der bestellten Produkte
  * @param beverageNr Anzahl der bestellten Getränke
  * @param dessert Name des bestellten Desserts
  * @param dessertNr Anzahl der bestellten Desserts
  *
  */
case class setUndeleteable(pizza: String, pizzaNr: Int, beverage: String, beverageNr: Int, dessert: String, dessertNr: Int) {

  if (pizzaNr != 0) {
    UndeleteableProducts.pizza = pizza
  }
  if (beverageNr != 0) {
    UndeleteableProducts.beverage = beverage
  }
  if (dessertNr != 0) {
    UndeleteableProducts.dessert = dessert
  }
}