package forms

/**
  * Form containing data to create an invoice.
  *
  * @author Hasibullah Faroq
  *
  * @param names    Name of the product of the invoice.
  * @param sizes    Size of the product of the invoice.
  * @param numbers  Number of the product of the invoice.
  */
case class CreateBillForm(names: List[String], sizes: List[String], numbers: List[Int])