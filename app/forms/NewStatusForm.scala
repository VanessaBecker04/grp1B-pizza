package forms

/**
  * Form containing data to set a new status of an order.
  *
  * @author Hasibullah Faroq
  */
case class NewStatusForm(var orderID: Long, var status: String)