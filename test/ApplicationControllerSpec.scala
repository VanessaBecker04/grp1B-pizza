import controllers.{Application, OrderController}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers.{GET, POST, OK, SEE_OTHER, contentAsString, defaultAwaitTimeout, redirectLocation, running, status}
import play.api.test._
import views.html.index

@RunWith(classOf[JUnitRunner])
class ApplicationControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "Application" should {

    "show the home page within a browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port)
      browser.pageSource must contain("Frische Pizza aus dem Steinofen")
    }

    "show the menu page within a browser" in new WithBrowser(app = FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    ))) {
      browser.goTo("http://localhost:" + port + "/showMenu")
      browser.pageSource must contain("Willkommen zur Speisekarte")
    }

    "show the login page within a browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port + "/login")
      browser.pageSource must contain("Login:")
    }

    "show the register page within a browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port + "/register")
      browser.pageSource must contain("Registrieren")
    }

    "show the contact page within a browser" in new WithBrowser {
      browser.goTo("http://localhost:" + port + "/contact")
      browser.pageSource must contain("Kontakt:")
    }

    "not show the editOrders view as a regular employee" in new WithApplication {
      val request = FakeRequest(POST, "/editOrders").withSession(
        "role" -> "Kunde"
      )
      val result = OrderController.editOrders()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Zugriffsrechte")
    }

    "show the index view when not logged in" in new WithApplication {
      val request = FakeRequest(POST, "/")
      val result = Application.index()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Pizza Suez")
    }

    "show the index View when logged in" in new WithApplication {
      val request = FakeRequest(POST, "/").withSession(
        "user" -> "-10"
      )
      val result = Application.index()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/welcomeUser")
    }

    "show the register view" in new WithApplication {
      val request = FakeRequest(POST, "/register")
      val result = Application.register()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Registrieren:")
    }

    "show the login view" in new WithApplication {
      val request = FakeRequest(POST, "/login")
      val result = Application.login()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Login:")
    }

    "show the contact view" in new WithApplication {
      val request = FakeRequest(POST, "/contact")
      val result = Application.contact()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Kontakt:")
    }
  }
}