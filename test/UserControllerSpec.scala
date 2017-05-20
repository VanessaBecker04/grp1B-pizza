import controllers.UserController
import dbaccess.UserDao
import models.User
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{running, status, redirectLocation, contentAsString, SEE_OTHER, OK, GET, POST}
import play.api.test.{FakeApplication, FakeRequest}

@RunWith(classOf[JUnitRunner])
class UserControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "UserController" should {

    "add a user" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      )
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/welcomeUser")
    }

    "add a user outside of delivery area" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "85521",
        "Stadt" -> "Ottobrunn",
        "Rolle" -> "Kunde"
      )
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=register")
    }

    "add an existing user" in memDB {
      UserDao.addUser(User(-1, "paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde", false))
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      )
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=emailused")
    }

    "add a user as an user" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      ).withSession("role" -> "Kunde")
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=permissiondenied")
    }

    "add a user as an employee" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      ).withSession("role" -> "Mitarbeiter")
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptSuccessful?successcode=usercreated")
    }

    "add a user with preexisting order" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      ).withSession(
        "orderedProducts" -> "1x Margarita (medium)",
        "sumOfOrder" -> "6.21"
      )
      val result = UserController.addUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/setOrder?orderedProducts=1x+Margarita+%28medium%29&sumOfOrder=6.21")
    }

    "edit a user" in memDB {
      val request = FakeRequest(POST, "/editUser").withFormUrlEncodedBody(
        "Kunden-ID" -> "-10",
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "82346",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      )
      val result = UserController.editUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptSuccessful?successcode=useredited")
    }

    "edit a user to outside of delivery area" in memDB {
      val request = FakeRequest(POST, "/editUser").withFormUrlEncodedBody(
        "Kunden-ID" -> "-10",
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1",
        "Vorname" -> "Paula",
        "Name" -> "Huber",
        "Straße und Hausnummer" -> "Zeisigweg 4",
        "Postleitzahl" -> "85521",
        "Stadt" -> "München",
        "Rolle" -> "Kunde"
      )
      val result = UserController.editUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=register")
    }

    "delete a user" in memDB {
      val request = FakeRequest(POST, "/deleteUser").withFormUrlEncodedBody(
        "Kunden-ID" -> "-10"
      )
      val result = UserController.deleteUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptSuccessful?successcode=userdeleted")
    }

    "show welcomeUser View" in memDB {
      val request = FakeRequest(POST, "/welcomeUser").withSession(
        "role" -> "Kunde"
      )
      val result = UserController.welcomeUser()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Kunde")
    }

    "show welcomeEmployee View" in memDB {
      val request = FakeRequest(GET, "/welcomeUser").withSession(
        "role" -> "Mitarbeiter"
      )
      val result = UserController.welcomeUser()(request)
      status(result) must equalTo(SEE_OTHER)
      val redirect = FakeRequest(GET, "/" + redirectLocation(result).get)
      val page = UserController.welcomeEmployee()(redirect)
      status(page) must equalTo(OK)
      contentAsString(page) must contain ("Mitarbeiter")
    }

    "show attemptFailed View" in memDB {
      val request = FakeRequest(POST, "/attemptFailed")
      val result = UserController.attemptFailed("loginfailed")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("fehlerhaft")
    }

    "show attemptSuccessful View" in memDB {
      val request = FakeRequest(POST, "/attemptSuccessful")
      val result = UserController.attemptSuccessful("usercreated")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("hinzugefügt")
    }

    "show showUsers View" in memDB {
      val request = FakeRequest(POST, "/showUsers")
      val result = UserController.showUsers()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Padrone")
    }

    "log in user" in memDB {
      val request = FakeRequest(POST, "/loginUser").withFormUrlEncodedBody(
        "Email" -> "emil@gmx.de",
        "Passwort" -> "Susanne82343"
      )
      val result = UserController.loginUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/welcomeUser")
    }

    "log in user with preexisting order" in memDB {
      val request = FakeRequest(POST, "/loginUser").withFormUrlEncodedBody(
        "Email" -> "emil@gmx.de",
        "Passwort" -> "Susanne82343"
      ).withSession(
        "orderedProducts" -> "1x Margarita (medium)",
        "sumOfOrder" -> "6.21"
      )
      val result = UserController.loginUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/setOrder?orderedProducts=1x+Margarita+%28medium%29&sumOfOrder=6.21")
    }

    "log in unexisting user" in memDB {
      val request = FakeRequest(POST, "/loginUser").withFormUrlEncodedBody(
        "Email" -> "",
        "Passwort" -> ""
      )
      val result = UserController.loginUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=loginfailed")
    }

    "log in inactive user" in memDB {
      UserDao.addUser(User(-1, "paula@gmx.de", "1", "Paula", "Huber", "Zeisigweg 4", 82346, "München", "Kunde", true))
      val request = FakeRequest(POST, "/loginUser").withFormUrlEncodedBody(
        "Email" -> "paula@gmx.de",
        "Passwort" -> "1"
      )
      val result = UserController.loginUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=inactive")
    }

    "log in employee" in memDB {
      val request = FakeRequest(GET, "/welcomeUser").withSession(
        "role" -> "Mitarbeiter"
      )
      val result = UserController.welcomeUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/welcomeEmployee")
    }

    "log out user" in memDB {
      val request = FakeRequest(GET, "/logoutUser")
      val result = UserController.logoutUser()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/")
    }

    "show editUsers View" in memDB {
      val request = FakeRequest(GET, "/editUsers").withSession(
        "role" -> "Mitarbeiter"
      )
      val result = UserController.editUsers()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Kunden ändern:")
    }

    "show editUsers View if not employee" in memDB {
      val request = FakeRequest(GET, "/editUsers").withSession(
        "role" -> "Kunde"
      )
      val result = UserController.editUsers()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Zugriffsrechte")
    }
  }
}