package forms

/**
  * Form containing data to edit a property of an existing product.
  *
  * @author Hasibullah Faroq
  *
  * @param id     Number of the existing product which should be changed.
  * @param name   New name of the product.
  * @param price  New price of the product which should be changed.
  * @param active True worth, if the product is active  or inactive.
  */
case class EditMenuForm(id: Long, name: String, price: Double, active: Boolean)