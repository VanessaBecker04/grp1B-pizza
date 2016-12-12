package models

/**
 * User entity.
 * @param id database id of the user.
 * @param name name of the user.
 */
case class User(var id: Long, var forename: String, var name: String, var address: String, var zipcode: Int, var city: String, var role: String)
