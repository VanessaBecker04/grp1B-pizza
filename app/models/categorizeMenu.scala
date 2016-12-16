package models

import scala.collection.mutable.ListBuffer

/**
  * Created by Hasi on 16.12.2016.
  */
object categorizeMenu {

  val pizzaList = new ListBuffer[String]
  val beverageList = new ListBuffer[String]
  val dessertList = new ListBuffer[String]

  val menuList = services.MenuService.addedToMenu
  for(m <- menuList) {
    if(m.category.equals("Pizza")) {
      categorizeMenu.pizzaList += m.name
    }
    if(m.category.equals("Getränk")) {
      categorizeMenu.beverageList += m.name
    }
    if(m.category.equals("Dessert")) {
      categorizeMenu.dessertList += m.name
    }
  }

}
