package models

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasi on 16.12.2016.
  */
object categorizeMenu {

  var pizzaList: List[String] =_
  var beverageList: List[String] =_
  var dessertList: List[String] =_
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
