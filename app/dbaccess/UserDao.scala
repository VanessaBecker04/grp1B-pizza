package dbaccess

import anorm.NamedParameter.symbol
import anorm.SqlParser._
import anorm.{SQL, _}
import models.{User, setUser}
import play.api.Play.current
import play.api.db.DB

/**
  * Data access object for user related operations.
  *
  * @author ob, scs, Maximilian Öttl
  */
trait UserDaoT {

  /**
    * Creates the given user in the database.
    *
    * @param user the user object to be stored.
    * @return the persisted user object
    */
  def addUser(user: User): User = {
    var returnuser: User = null
    DB.withConnection { implicit c =>
      val selectDuplicate = SQL("Select TOP 1 email from Users where email = {email}").on(
        'email -> user.email).as(scalar[String].singleOpt)
      if (selectDuplicate.isDefined) {
        returnuser = null
      } else {
        returnuser = user
        val id: Option[Long] =
          SQL("insert into Users(email, password, forename, name, address, zipcode, city, role, inactive) values ({email}, {password}, {forename}, {name}, {address}, {zipcode}, {city}, {role}, {inactive})").on(
            'email -> user.email, 'password -> user.password, 'forename -> user.forename, 'name -> user.name, 'address -> user.address, 'zipcode -> user.zipcode, 'city -> user.city, 'role -> user.role, 'inactive -> user.inactive).executeInsert()
        user.id = id.get
        if (!models.activeUser.role.equals("Mitarbeiter")) {
          setUser(id.get, user.forename, user.name, user.address, user.zipcode, user.city, user.role)
        }
      }
    }
    returnuser
  }

  /**
    * Edits the given user in the database.
    *
    * @param user the user object to be stored.
    * @return the persisted user object
    */
  def editUser(user: User): User = {
    var returnuser: User = null
    DB.withConnection { implicit c =>
      val selectDuplicate = SQL("Select TOP 1 email from Users where email = {email}").on(
        'email -> user.email).as(scalar[String].singleOpt)
      if (selectDuplicate.isDefined) {
        returnuser = null
      } else {
        returnuser = user
        SQL("Update Users set email={email}, password={password}, forename={forename}, name={name}, address={address}, zipcode={zipcode}, city={city}, role={role}, inactive={inactive} where id = {id}").on(
          'id -> user.id, 'email -> user.email, 'password -> user.password, 'forename -> user.forename, 'name -> user.name, 'address -> user.address, 'zipcode -> user.zipcode, 'city -> user.city, 'role -> user.role, 'inactive -> user.inactive).executeUpdate()
      }
    }
    returnuser
  }

  /**
    * Removes a user by id from the database.
    *
    * @param id the users id
    * @return a boolean success flag
    */
  def deleteUser(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val userOrders = OrderHistoryDao.showOrdersUser(id)
      if (userOrders.isEmpty) {
        val rowsCount = SQL("delete from Users where id = {id}").on('id -> id).executeUpdate()
        rowsCount > 0
      } else {
        val rowsCount = SQL("Update Users set inactive=B'1' where id = {id}").on('id -> id).executeUpdate()
        rowsCount > 0
      }
    }
  }

  /**
    * Returns a list of available user from the database.
    *
    * @return a list of user objects.
    */
  def registeredUsers: List[User] = {
    DB.withConnection { implicit c =>
      val selectUsers = SQL("Select id, email, forename, name, address, zipcode, city, role, inactive from Users")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val users = selectUsers().map(row => User(row[Long]("id"), row[String]("email"), '1'.toString, row[String]("forename"), row[String]("name"), row[String]("address"), row[Int]("zipcode"), row[String]("city"), row[String]("role"), row[Boolean]("inactive"))).toList
      users
    }
  }

  /**
    * Returns the customerID of the loggedinUser if successful. Returns errorcode if login was not successful.
    *
    * @return customerID
    */
  def loginUser(email: String, password: String): Long = {
    DB.withConnection { implicit c =>
      val selectUser = SQL("Select id from Users where (email = {email}) AND (password = {password})").on(
        'email -> email, 'password -> password).as(scalar[Long].singleOpt)
      val selectInactive = SQL("Select inactive from Users where (email = {email}) AND (password = {password})").on(
        'email -> email, 'password -> password).as(scalar[Boolean].singleOpt)
      if (selectUser.isEmpty) {
        -1
      } else if (selectInactive.get) {
        -2
      } else {
        setActiveUser(selectUser.get)
        selectUser.get
      }
    }
  }

  /**
    * The logged in user is set as the active user.
    */
  def setActiveUser(idgiven: Long): Unit = {
    DB.withConnection { implicit c =>
      val selectedUser = SQL("Select id, forename, name, address, zipcode, city, role from Users where id = {idgiven}").on('idgiven -> idgiven)
      selectedUser().map(row => setUser(row[Long]("id"), row[String]("forename"), row[String]("name"), row[String]("address"), row[Int]("zipcode"), row[String]("city"), row[String]("role")))
    }
  }
}

object UserDao extends UserDaoT
