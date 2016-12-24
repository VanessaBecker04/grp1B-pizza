package services

import dbaccess.{UserDao, UserDaoT}
import models.{User, setUser}

/**
  * Service class for user related operations.
  *
  * @author ob, scs
  */
trait UserServiceT {

  val userDao: UserDaoT = UserDao

  /**
    * Adds a new user to the system.
    *
    * @param name name of the new user.
    * @return the new user.
    */
  def addUser(forename: String, name: String, address: String, zipcode: Int, city: String, role: String): User = {
    // create User
    val inactive: Boolean = false
    val newUser = User(-1, forename, name, address, zipcode, city, role, inactive)
    // persist and return User
    userDao.addUser(newUser)
  }

  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
    */
  def deleteUser(id: Long): Boolean = userDao.deleteUser(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def registeredUsers: List[User] = {
    userDao.registeredUsers
  }

  def loginUser(name: String, zipcode: Int): Long = {
    userDao.loginUser(name, zipcode)
  }

  def logoutUser(): Unit = {
    services.OrderService.cancelOrder()
    setUser(0, null, null, null, 0, null, null)
  }
}

object UserService extends UserServiceT
