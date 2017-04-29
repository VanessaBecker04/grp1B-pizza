package forms

/**
  * Created by Hasibullah Faroq on 25.11.2016.
  *
  * Form beinhaltet Daten, um eine Rechnung zu erstellen.
  */
case class CreateBillForm(names: List[String], sizes: List[String], numbers: List[Int])