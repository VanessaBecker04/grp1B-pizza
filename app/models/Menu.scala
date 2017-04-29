package models

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  */
case class Menu(var id: Long, var name: String, var price: Double, var unit: String, var category: String, var ordered: Boolean, var active: Boolean)
case class CategoryPlusUnit(var category: String, var unit: String)