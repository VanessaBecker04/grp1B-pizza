package models

/**
  * Created by Hasi on 28.11.2016.
  */
case class Bill (var id: Long, var costumerId: Long, var pizza: String, var beverage: String, var dessert: String,
                 var pizzaSize: Boolean, var beverageSize: Boolean)
