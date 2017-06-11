package models

/**
  * Entity of the menu.
  *
  * @param id       ID of the product.
  * @param name     Name of the product.
  * @param price    Price of the product.
  * @param unit     Unit of the product.
  * @param category Category of the product.
  * @param ordered  Shows whether the product has already been ordered.
  * @param active   Shows whether the product is active.
  *
  * @author Hasibullah Faroq.
  */
case class Menu(var id: Long, var name: String, var price: Double, var unit: String, var category: String, var ordered: Boolean, var active: Boolean)