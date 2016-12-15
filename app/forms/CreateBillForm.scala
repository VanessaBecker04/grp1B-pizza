package forms

/**
  * Created by Hasi on 25.11.2016.
  */
case class CreateBillForm(var customerID: Long, var pizzaName: String, var pizzaNumber: Int,
                          var pizzaSize: String, var beverageName: String, var beverageNumber: Int,
                          var beverageSize: String, var dessertName: String, var dessertNumber: Int)
