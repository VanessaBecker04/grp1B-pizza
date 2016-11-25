package services

import dbaccess.{UserDao, UserDaoT}
import models.User

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait UserServiceT {

  val userDao: UserDaoT = UserDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addUser(forename: String, name: String, address: String, zipcode: String, city: String, role: String): User = {
    // create User
    val newUser = User(-1, forename, name, address, zipcode, city, role)
    // persist and return User
    userDao.addUser(newUser)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmUser(id: Long): Boolean = userDao.rmUser(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def registeredUsers: List[User] = {
    userDao.registeredUsers
  }

  def loginUser(name: String, zipcode: String): List[Long] = {
    userDao.loginUser(name, zipcode)
  }

}

object UserService extends UserServiceT
