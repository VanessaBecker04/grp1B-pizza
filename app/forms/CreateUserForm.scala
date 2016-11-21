package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreateUserForm(forename: String, name: String, address: String, zipcode: String, city: String, role: String)
