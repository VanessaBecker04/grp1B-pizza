package forms

/**
  * Created by Hasibullah Faroq on 25.11.2016.
  *
  * Form beinhaltet Daten um eine Rechnung zu erstellen.
  *
  * @param pizzaName      Name der bestellten Pizza
  * @param pizzaNumber    Anzahl der bestellen Pizzen
  * @param pizzaSize      größe der Pizza
  * @param beverageName   Name der bestellten Getränkes
  * @param beverageNumber Anzahl der bestellen Getränke
  * @param beverageSize   größe des bestellten Getränkes
  * @param dessertName    Name der bestellten Desserts
  * @param dessertNumber  Anzahl der bestellen Desserts
  */
case class CreateBillForm(var p1Name: String,
                          var p1Size: String,
                          var p1Number: Int,
                          var p2Name: String,
                          var p2Size: String,
                          var p2Number: Int,
                          var p3Name: String,
                          var p3Size: String,
                          var p3Number: Int)