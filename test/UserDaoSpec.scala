import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import dbaccess.{OrderDao, UserDao}
import models.{OrderHistory, User}

@RunWith(classOf[JUnitRunner])
class UserDaoSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The UserDao" should {

    "add a new user" in memDB {
      UserDao.addUser(User(-1, "paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde", false)) must be_!==(None)
    }

    "add an existing user" in memDB {
      UserDao.addUser(User(-1, "padrone@suez.de", "", "", "", "", 1, "", "", false)) must be_!==(None)
    }

    "edit a user" in memDB {
      val user = User(-20, "paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde", false)
      UserDao.editUser(user) must be_==(user)
    }

    "delete a user" in memDB {
      UserDao.deleteUser(-10) must beTrue
    }

    "delete an unexisting user" in memDB {
      UserDao.deleteUser(-1) must beFalse
    }

    "delete a user with existing orders" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -10, "Herbert Padrone, Kientalstr. 10, 82346 Andechs", "1x Regina (medium)", 7.29, "20.05.2017", "in Bearbeitung"))
      UserDao.deleteUser(-10) must beTrue
    }

    "return list of users" in memDB {
      UserDao.registeredUsers.length must be equalTo 2
    }

    "login user" in memDB {
      UserDao.loginUser("padrone@suez.de", "Suez82346").id must be equalTo -10
    }
  }
}