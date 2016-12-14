package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.{User, setUser}
import anorm.SqlParser._
import anorm._

/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait UserDaoT {

  /**
   * Creates the given user in the database.
   * @param user the user object to be stored.
   * @return the persisted user object
   */
  def addUser(user: User): User = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Users(forename, name, address, zipcode, city, role) values ({forename},{name},{address},{zipcode},{city},{role})").on(
          'forename -> user.forename, 'name -> user.name, 'address -> user.address, 'zipcode -> user.zipcode, 'city -> user.city, 'role -> user.role).executeInsert()
      user.id = id.get
      setUser(id.get, user.forename, user.name, user.address, user.zipcode, user.city, user.role)
    }
    user
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmUser(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Users where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def registeredUsers: List[User] = {
    DB.withConnection { implicit c =>
      val selectUsers = SQL("Select id, forename, name, address, zipcode, city, role from Users")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val users = selectUsers().map(row => User(row[Long]("id"), row[String]("forename"), row[String]("name"), row[String]("address"), row[Int]("zipcode"), row[String]("city"), row[String]("role"))).toList
      users
    }
  }

  def loginUser(namegiven: String, zipcodegiven: Int): Unit = {
    DB.withConnection { implicit c =>
      val selectUser = SQL("Select id from Users where (name = {namegiven}) AND (zipcode = {zipcodegiven})").on(
        'namegiven -> namegiven, 'zipcodegiven -> zipcodegiven).as(scalar[Long].singleOpt)
      if (selectUser.isEmpty) {
        -1
      } else {
        setActiveUser(selectUser.get)
      }
    }
  }
  def setActiveUser(idgiven: Long): Unit = {
    DB.withConnection { implicit c =>
      val selectedUser = SQL("Select id, forename, name, address, zipcode, city, role from Users where id = {idgiven}").on('idgiven -> idgiven)
      selectedUser().map(row => setUser(row[Long]("id"), row[String]("forename"), row[String]("name"), row[String]("address"), row[Int]("zipcode"), row[String]("city"), row[String]("role")))
    }
  }
}

object UserDao extends UserDaoT
