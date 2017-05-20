import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.running
import play.api.test.FakeApplication
import services.UserService

@RunWith(classOf[JUnitRunner])
class UserServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The UserService" should {

    "add a new user" in memDB {
      UserService.addUser("paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde") must be_!==(None)
    }

    "edit a user" in memDB {
      UserService.editUser(-20, "paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde") must be_!==(None)
    }

    "delete a user" in memDB {
      UserService.deleteUser(-10) must beTrue
    }

    "delete an unexisting user" in memDB {
      UserService.deleteUser(1) must beFalse
    }

    "return list of users" in memDB {
      UserService.registeredUsers.length must be equalTo 2
    }

    "login user" in memDB {
      UserService.loginUser("padrone@suez.de", "Suez82346").id must be equalTo -10
    }

  }
}