package forms

import java.util.Date

/**
  * Created by Hasibullah Faroq on 14.11.2016.
  *
  * Form beinhaltet Daten um ein Bestellverlauf für einen Kunden zu erstellen.
  *
  * @param orderID         Bestellnummer
  * @param customerID      Kundennummer
  * @param customerData    Daten des Kunden
  * @param orderedProducts bestellte Produkte
  * @param sumOfOrder      Gesamtsumme der Bestellung
  * @param orderDate       Datum der Bestellung
  */
case class CreateOrderHistoryForm(orderID: Long, customerID: Long, customerData: String, orderedProducts: String,
                                  sumOfOrder: Double, orderDate: Date)
