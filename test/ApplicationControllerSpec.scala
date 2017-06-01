import controllers.{Application, OrderController}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers.{GET, POST, OK, SEE_OTHER, contentAsString, defaultAwaitTimeout, redirectLocation, running, status}
import play.api.test._
import views.html.index

/**
  * add your integration spec here.
  * An integration test will fire up a whole play application in a real (or headless) browser
  */
@RunWith(classOf[JUnitRunner])
class ApplicationControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "Application" should {

    "work from within a browser" in new WithBrowser {

      browser.goTo("http://localhost:" + port)

      browser.pageSource must contain("Frische Pizza aus dem Steinofen")
    }
  }
/*
  "Application" should {

      "work from within a browser" in new WithBrowser with memDB{

        browser.goTo("http://localhost:" + port + "/showMenu")

        browser.pageSource must contain("Willkommen zur Speisekarte")
      }
  }
  */

  "Application" should {

    "work from within a browser" in new WithBrowser {

      browser.goTo("http://localhost:" + port + "/login")

      browser.pageSource must contain("Login:")
    }
  }

  "Application" should {

    "work from within a browser" in new WithBrowser {

      browser.goTo("http://localhost:" + port + "/register")

      browser.pageSource must contain("Registrieren")
    }
  }

  "Application" should {

    "work from within a browser" in new WithBrowser {

      browser.goTo("http://localhost:" + port + "/contact")

      browser.pageSource must contain("Kontakt:")
    }
  }

  "show editOrders View if not employee" in new WithApplication {
    val request = FakeRequest(POST, "/editOrders").withSession(
      "role" -> "Kunde"
    )
    val result = OrderController.editOrders()(request)
    status(result) must equalTo(OK)
    contentAsString(result) must contain("Zugriffsrechte")
  }

  "show Index view if not logged in" in new WithApplication {
    val request = FakeRequest(POST, "/")
    val result = Application.index()(request)
    status(result) must equalTo(OK)
    contentAsString(result) must contain("Pizza Suez")
  }

  "show Index View if logged in" in new WithApplication {
    val request = FakeRequest(POST, "/").withSession(
      "user" -> "-10"
    )
    val result = Application.index()(request)
    status(result) must equalTo(SEE_OTHER)
    redirectLocation(result) must beSome("/welcomeUser")
  }

  "show Register view" in new WithApplication {
    val request = FakeRequest(POST, "/register")
    val result = Application.register()(request)
    status(result) must equalTo(OK)
    contentAsString(result) must contain("Registrieren:")
  }

  "show Login view" in new WithApplication {
    val request = FakeRequest(POST, "/login")
    val result = Application.login()(request)
    status(result) must equalTo(OK)
    contentAsString(result) must contain("Login:")
  }

  "show Contact view" in new WithApplication {
    val request = FakeRequest(POST, "/contact")
    val result = Application.contact()(request)
    status(result) must equalTo(OK)
    contentAsString(result) must contain("Kontakt:")
  }

}

