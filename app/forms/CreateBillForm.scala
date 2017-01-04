package forms

/**
  * Created by Hasibullah Faroq on 25.11.2016.
  *
  * Form beinhaltet Daten um eine Rechnung zu erstellen.
  *
  * @param customerID     id des Kunden
  * @param pizzaName      Name der bestellten Pizza
  * @param pizzaNumber    Anzahl der bestellen Pizzen
  * @param pizzaSize      größe der Pizza
  * @param beverageName   Name der bestellten Getränkes
  * @param beverageNumber Anzahl der bestellen Getränke
  * @param beverageSize   größe des bestellten Getränkes
  * @param dessertName    Name der bestellten Desserts
  * @param dessertNumber  Anzahl der bestellen Desserts
  */
case class CreateBillForm(var customerID: Long, var pizzaName: String, var pizzaNumber: Int,
                          var pizzaSize: String, var beverageName: String, var beverageNumber: Int,
                          var beverageSize: String, var dessertName: String, var dessertNumber: Int)
