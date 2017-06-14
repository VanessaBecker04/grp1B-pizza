package forms

/** Form containing data to add products.
  *
  * @author Maximilian Öttl
  *
  * @param names    Name of the product
  * @param sizes    Size of the product
  * @param numbers  Number of the product
  */
case class CreateBillForm(names: List[String], sizes: List[String], numbers: List[Int])