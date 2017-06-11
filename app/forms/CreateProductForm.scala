package forms

/**
  * Form containing data to create a product.
  *
  * @author Hasibullah Faroq
  *
  * @param name     Name of the product.
  * @param price    Price per €/cm of the product.
  * @param category Category of the product.
  */
case class CreateProductForm(name: String, price: Double, category: String)