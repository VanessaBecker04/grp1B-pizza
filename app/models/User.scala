package models

/**
 * User entity.
 * @param id database id of the user.
 * @param name name of the user.
 */
case class User(var id: Long, var forename: String, name: String, address: String, zipcode: String, city: String, role: String)
