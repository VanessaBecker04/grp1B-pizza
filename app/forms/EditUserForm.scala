package forms

/**
  * Form containing data to create a user.
  *
  * @param name name of the user.
  */
case class EditUserForm(customerID: Long, email: String, password: String, forename: String, name: String, address: String, zipcode: Int, city: String, role: String)
