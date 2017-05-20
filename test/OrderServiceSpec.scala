import models.{Bill, Product}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.OrderService

@RunWith(classOf[JUnitRunner])
class OrderServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The OrderService" should {

    "calculate bill" in memDB {
      val product1 = Product("Margarita", "medium", 1)
      val product2 = Product("Regina", "medium", 1)
      val bill = Bill(List(product1, product2))
      val (orderedProducts, sumOfOrder) = OrderService.doCalculationForBill(bill)
      orderedProducts must be equalTo "1x Margarita (medium), 1x Regina (medium)"
      sumOfOrder must be equalTo 13.5
    }

    "calculate delivery time inside of delivery area" in memDB {
      OrderService.calculateDeliveryTime(82346) must be equalTo 42
    }

    "calculate delivery time outside of delivery area" in memDB {
      OrderService.calculateDeliveryTime(85521) must be equalTo -1
    }

    "add an order to history" in memDB {
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017") must !==(None)
    }

    "delete an order" in memDB {
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017")
      OrderService.rmFromHistory(1) must beTrue
    }

    "delete an unexisting order" in memDB {
      OrderService.rmFromHistory(-1) must beFalse
    }

    "return a list of all orders" in memDB {
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017")
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Regina (medium)", 7.29, "20.05.2017")
      val orders = OrderService.showOrdersEmployee
      orders.length must be equalTo 2
      orders(0).orderedProducts.contains("Margarita")
      orders(1).orderedProducts.contains("Regina")
    }

    "return a list of orders from customer" in memDB {
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017")
      OrderService.addToHistory(-10, "Herbert Padrone, Kientalstr. 10, 82346 Andechs", "1x Regina (medium)", 7.29, "20.05.2017")
      val orders = OrderService.showOrdersUser(-20)
      orders.length must be equalTo 1
      orders.head.orderedProducts.contains("Margarita")
    }

    "update status for order" in memDB {
      OrderService.addToHistory(-20, "Susanne Emil, Ulrichstr. 1, 82343 Pöcking", "1x Margarita (medium)", 6.21, "20.05.2017")
      OrderService.setStatusForOrder(1, "in Auslieferung")
      val orders = OrderService.showOrdersUser(-20)
      orders.head.status must be equalTo "in Auslieferung"
    }
  }
}