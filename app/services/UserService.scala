package services

import dbaccess.{UserDao, UserDaoT}
import models.User

/** Service class for user related operations.
  *
  * @author ob, scs, Maximilian Öttl
  */
trait UserServiceT {

  val userDao: UserDaoT = UserDao

  /** Adds a new user to the database.
    *
    * @param name name of the new user
    * @return the new user
    */
  def addUser(email: String, password: String, forename: String, name: String, address: String, zipcode: Int, city: String, role: String): User = {
    // create User
    val inactive: Boolean = false
    val newUser = User(-1, email, password, forename, name, address, zipcode, city, role, inactive)
    // persist and return User
    userDao.addUser(newUser)
  }

  /** Edits a user of the database.
    *
    * @param name name of the edited user
    * @return the edited user
    */
  def editUser(customerID: Long, email: String, password: String, forename: String, name: String, address: String, zipcode: Int, city: String, role: String): User = {
    // create User
    val inactive: Boolean = false
    val editedUser = User(customerID, email, password, forename, name, address, zipcode, city, role, inactive)
    // persist and return User
    userDao.editUser(editedUser)
  }

  /** Removes a user by id from the database.
    *
    * @param id user id
    * @return a boolean success flag
    */
  def deleteUser(id: Long): Boolean = userDao.deleteUser(id)

  /** Gets a list of all registered users.
    *
    * @return list of users.
    */
  def registeredUsers: List[User] = {
    userDao.registeredUsers
  }

  /** Logs in a user.
    *
    * @param email    email
    * @param password password
    * @return
    */
  def loginUser(email: String, password: String): Either[String, User] = {
    userDao.loginUser(email, password)
  }
}

object UserService extends UserServiceT