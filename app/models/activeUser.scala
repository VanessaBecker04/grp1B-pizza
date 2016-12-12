package models

/**
  * The active user currently logged in.
  *
  * @author ob, scs
  */
object activeUser {

  var id : Long = 0
  var orderID : Long = 0
  var forename : String = _
  var name : String = _
  var address : String = _
  var zipcode : Int = _
  var city : String = _
  var role : String = _

}

case class setUser(id: Long, forename: String, name: String, address: String, zipcode: Int, city: String, role: String) {
  activeUser.id = id
  activeUser.forename = forename
  activeUser.name = name
  activeUser.address = address
  activeUser.zipcode = zipcode
  activeUser.city = city
  activeUser.role = role
}
