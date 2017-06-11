package models

/**
  * Entity of the user.
  *
  * @param id       Database id of the user.
  * @param email    E-Mail-Address of the user.
  * @param password Password of the user.
  * @param forename Forename of the user.
  * @param name     Name of the user.
  * @param address  Adress of the user.
  * @param zipcode  Zipcode of the user.
  * @param city     City of the user.
  * @param role     Role of the user.
  * @param inactive Shows whether the status of the user is inactive.
  */
case class User(var id: Long, var email: String, var password: String, var forename: String, var name: String,
                var address: String, var zipcode: Int, var city: String, var role: String, var inactive: Boolean)