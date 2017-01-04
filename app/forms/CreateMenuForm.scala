package forms

/**
  * Created by Hasibullah Faroq on 21.11.2016.
  *
  * Form beinhaltet Daten um ein Produkt zu erstellen.
  *
  * @param name name des Produktes
  * @param price €/cm des Produktes
  * @param category Kategorie des Produktes
  */
case class CreateMenuForm(name: String, price: Double, category: String)
