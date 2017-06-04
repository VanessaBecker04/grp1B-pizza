/**
  * Created by Hasib on 30.05.2017.
  */

import controllers.BillController
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{GET, OK, POST, SEE_OTHER, contentAsString, defaultAwaitTimeout, redirectLocation, running, status}
import play.api.test.{FakeApplication, FakeRequest, WithApplication}

@RunWith(classOf[JUnitRunner])
class BillControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "BillController" should {

    "add simple Order with one Product to Cart" in memDB {
      val request = FakeRequest(POST, "/addToBill").withFormUrlEncodedBody(
        "names[0]" -> "Regina",
        "sizes[0]" -> "medium",
        "numbers[0]" -> "2"
      )
      val result = BillController.addToBill()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/setOrder?orderedProducts=2x+Regina+%28medium%29&sumOfOrder=14.580000000000002")
    }

    "add Order with two Product to Cart" in memDB {
      val request = FakeRequest(POST, "/addToBill").withFormUrlEncodedBody(
        "names[0]" -> "Regina",
        "sizes[0]" -> "medium",
        "numbers[0]" -> "2",
        "names[1]" -> "Margarita",
        "sizes[1]" -> "large",
        "numbers[1]" -> "3"
      )
      val result = BillController.addToBill()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/setOrder?orderedProducts=2x+Regina+%28medium%29%2C+3x+Margarita+%28large%29&sumOfOrder=36.660000000000004")
    }

    "add simple Order with one Product with number < 1" in memDB {
      val request = FakeRequest(POST, "/addToBill").withFormUrlEncodedBody(
        "names[0]" -> "Regina",
        "sizes[0]" -> "medium",
        "numbers[0]" -> "0"
      )
      val result = BillController.addToBill()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=atLeastOneProduct")
    }

    "add Order with two Product with number < 1" in memDB {
      val request = FakeRequest(POST, "/addToBill").withFormUrlEncodedBody(
        "names[0]" -> "Regina",
        "sizes[0]" -> "medium",
        "numbers[0]" -> "0",
        "names[1]" -> "Margarita",
        "sizes[1]" -> "large",
        "numbers[1]" -> "0"
      )
      val result = BillController.addToBill()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/attemptFailed?errorcode=atLeastOneProduct")
    }

    "add Order with two Product. One with number >= 1 and another One with number < 1" in memDB {
      val request = FakeRequest(POST, "/addToBill").withFormUrlEncodedBody(
        "names[0]" -> "Regina",
        "sizes[0]" -> "medium",
        "numbers[0]" -> "1",
        "names[1]" -> "Margarita",
        "sizes[1]" -> "large",
        "numbers[1]" -> "0"
      )
      val result = BillController.addToBill()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/setOrder?orderedProducts=1x+Regina+%28medium%29&sumOfOrder=7.290000000000001")
    }

    "show showBill View" in new WithApplication {
      val request = FakeRequest(GET, "/showBill")
      val result = BillController.showBill()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Warenkorb")
    }

    "cancel an order" in new WithApplication {
      val request = FakeRequest(GET, "/cancelOrder")
      val result = BillController.cancelOrder()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/showMenu")
    }

  }
}
