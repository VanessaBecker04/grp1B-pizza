package forms

/** Form containing data to edit a user.
  *
  * @param customerID   Number of the user which should be changed.
  * @param email        E-Mail-Address of the user which should be changed.
  * @param password     Passwaord of the user which should be changed.
  * @param forename     Forename of the user which should be changed.
  * @param name         Name of the user which should be changed.
  * @param address      Address of the user which should be changed.
  * @param zipcode      Zipcode of the user which should be changed.
  * @param city         City of the user which should be changed.
  * @param role         Role of the user which should be changed.
  */
case class EditUserForm(customerID: Long, email: String, password: String, forename: String, name: String, address: String, zipcode: Int, city: String, role: String)