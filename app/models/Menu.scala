package models

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasi on 21.11.2016.
  */
case class Menu(var id: Long, var name: String, var price: Double, var category: String, var ordered: Boolean)

object categorizeMenu {

  var pizzaList: List[String] =_
  var beverageList: List[String] =_
  var dessertList: List[String] =_
  var allIdFromMenu: List[Long] =_
}

case class categorize() {

  val pizzaList = new ListBuffer[String]
  val beverageList = new ListBuffer[String]
  val dessertList = new ListBuffer[String]

  val menuList = services.MenuService.addedToMenu
  for (m <- menuList) {
    if (m.category.equals("Pizza")) {
      pizzaList += m.name
    }
    if (m.category.equals("Getränk")) {
      beverageList += m.name
    }
    if (m.category.equals("Dessert")) {
      dessertList += m.name
    }
  }

  categorizeMenu.pizzaList = pizzaList.toList
  categorizeMenu.beverageList = beverageList.toList
  categorizeMenu.dessertList = dessertList.toList

}

case class putAllMenuIDInList() {
  val allID = new ListBuffer[Long]
  for (s <- services.MenuService.addedToMenu) {
    allID += s.id
  }
  categorizeMenu.allIdFromMenu = allID.toList
}

object UndeleteableProducts {
  var pizza: String =_
  var beverage: String =_
  var dessert: String =_
}

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