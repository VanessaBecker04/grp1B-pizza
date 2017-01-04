package models

/**
  * Created by Hasibullah Faroq on 28.11.2016.
  * Bill Entität
  */
case class Bill(var id: Long, var customerID: Long, var pizzaName: String, var pizzaNumber: Int,
                var pizzaSize: String, var beverageName: String, var beverageNumber: Int,
                var beverageSize: String, var dessertName: String, var dessertNumber: Int)
