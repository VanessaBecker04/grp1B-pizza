package forms

/**
  * Form containing data to create an user.
  *
  * @param email    E-Mail-Address of the user.
  * @param password Password of the user.
  * @param forename Forename of the user.
  * @param name     Name of the user.
  * @param address  Address of the user.
  * @param zipcode  Zipcode of the user.
  * @param city     City of the user.
  * @param role     Role of the user.


  */
case class CreateUserForm(email: String, password: String, forename: String, name: String, address: String, zipcode: Int, city: String, role: String)