import controllers.{MenuController, OrderController}
import dbaccess.OrderDao
import models.OrderHistory
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import play.api.test.Helpers.{BAD_REQUEST, OK, POST, SEE_OTHER, contentAsString, defaultAwaitTimeout, redirectLocation, running, status}
import services.{MenuService, OrderService}

@RunWith(classOf[JUnitRunner])
class OrderControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The OrderController" should {

    "add an order to history" in memDB {
      val request = FakeRequest(POST, "/addToHistory").withSession(
        "user" -> "-20",
        "customerData" -> "Susanne Emil, Ulrichstr. 1, 82343 Pöcking",
        "orderedProducts" -> "1x Margarita (medium)",
        "sumOfOrder" -> "6.21",
        "currentDate" -> "20.05.2017"
      )
      val result = OrderController.addToHistory()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/deliveryTime")
      MenuService.listOfAllProducts.exists(p => p.name == "Margarita" && p.ordered) must beTrue
    }

    "show orders of user" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Regina (medium)", 7.29, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/showOrdersUser").withSession(
        "user" -> "-20"
      )
      val result = OrderController.showOrdersUser()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Margarita") contain "Regina" contain "13.5"
    }

    "show orders of user if not logged in" in memDB {
      val request = FakeRequest(POST, "/showOrdersUser")
      val result = OrderController.showOrdersUser()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Zugriffsrechte")
    }

    "show all orders" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.addToHistory(OrderHistory(-1, -10, "Herbert Padrone, Kientalstr. 10, 82346 Andechs", "1x Regina (medium)", 7.29, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/showOrdersEmployee").withSession(
        "role" -> "Mitarbeiter"
      )
      val result = OrderController.showOrdersEmployee()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Margarita") contain "Regina" contain "13.5"
    }

    "show all orders if not employee" in memDB {
      val request = FakeRequest(POST, "/showOrdersEmployee").withSession(
        "role" -> "Kunde"
      )
      val result = OrderController.showOrdersEmployee()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Zugriffsrechte")
    }

    "show user orders if employee" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/showOrdersEmployee").withFormUrlEncodedBody(
        "CustomerID" -> "-20"
      )
      val result = OrderController.showOrdersEmployeeU()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("6.21")
    }

    "cause a bad request when showing all orders without being logged in" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/showOrdersEmployee").withFormUrlEncodedBody(
      )
      val result = OrderController.showOrdersEmployeeU()(request)
      status(result) must equalTo(BAD_REQUEST)
    }

    "show deliveryTime View" in new WithApplication {
      val request = FakeRequest(POST, "/showDeliveryTime").withSession(
        "deliveryTime" -> "16"
      )
      val result = OrderController.showDeliveryTime()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("16")
    }

    "show editOrders View" in memDB {
      val request = FakeRequest(POST, "/editOrders").withSession(
        "role" -> "Mitarbeiter"
      )
      val result = OrderController.editOrders()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Alle Bestellungen")
    }

    "show editOrders View if not employee" in memDB {
      val request = FakeRequest(POST, "/editOrders").withSession(
        "role" -> "Kunde"
      )
      val result = OrderController.editOrders()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Zugriffsrechte")
    }

    "remove order from history" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/cancelOrderHistory")
      val result = OrderController.cancelOrderHistory(1)(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/showOrdersUser")
      OrderService.showOrdersUser(-20) must beEmpty
    }

    "set status for order" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/setStatusForOrder").withFormUrlEncodedBody(
        "BestellID" -> "1",
        "Neuer Status" -> "in Auslieferung"
      )
      val result = OrderController.setStatusForOrder()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/showOrdersEmployee")
      OrderService.showOrdersUser(-20).exists(o => o.status == "in Auslieferung") must beTrue
    }
    "cause a bad request when an order is edited with missing values" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      val request = FakeRequest(POST, "/setStatusForOrder").withFormUrlEncodedBody(
        "Neuer Status" -> "in Auslieferung"
      )
      val result = OrderController.setStatusForOrder()(request)
      status(result) must equalTo(BAD_REQUEST)
    }
  }
}