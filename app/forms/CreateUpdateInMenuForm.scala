package forms

/**
  * Created by Hasibullah Faroq on 27.12.2016.
  * Form beinhaltet Daten um Eigenschaften eines bestehenden Produktes abzuändern.
  *
  * @param id     id des bestehenden Produktes, welcher abgeändert werden soll
  * @param name   neuer Name des Produktes
  * @param price  neuer Preis vom Produkt
  * @param active Wahrheitswert, ob Produkt aktiv ist oder nicht
  */
case class CreateUpdateInMenuForm(id: Long, name: String, price: Double, active: Boolean)


