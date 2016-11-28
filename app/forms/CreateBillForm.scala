package forms

/**
  * Created by Hasi on 25.11.2016.
  */
case class CreateBillForm(customerID: Long, pizza: String, beverage: String, dessert: String,
                          pizzaSize: Boolean, beverageSize: Boolean)
