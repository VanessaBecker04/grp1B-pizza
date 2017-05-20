import dbaccess.OrderDao
import models.OrderHistory
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class OrderDaoSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The OrderDao" should {

    "add an order to history" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung")) must be_!==(None)
    }

    "delete an order" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.rmFromHistory(1) must beTrue
    }

    "delete an unexisting order" in memDB {
      OrderDao.rmFromHistory(-1) must beFalse
    }

    "return a list of all orders" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Regina (medium)", 7.29, "20.05.2017", "in Bearbeitung"))
      val orders = OrderDao.showOrdersEmployee
      orders.length must be equalTo 2
      orders(0).orderedProducts.contains("Margarita")
      orders(1).orderedProducts.contains("Regina")
    }

    "return a list of orders from customer" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.addToHistory(OrderHistory(-1, -10, "Herbert Padrone, Kientalstr. 10, 82346 Andechs", "1x Regina (medium)", 7.29, "20.05.2017", "in Bearbeitung"))
      val orders = OrderDao.showOrdersUser(-20)
      orders.length must be equalTo 1
      orders.head.orderedProducts.contains("Margarita")
    }

    "update status for order" in memDB {
      OrderDao.addToHistory(OrderHistory(-1, -20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017", "in Bearbeitung"))
      OrderDao.setStatusForOrder(1, "in Auslieferung")
      val orders = OrderDao.showOrdersUser(-20)
      orders.head.status must be equalTo "in Auslieferung"
    }
  }
}