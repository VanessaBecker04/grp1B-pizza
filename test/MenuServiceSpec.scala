import models.Menu
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.FakeApplication
import play.api.test.Helpers.running
import services.MenuService

@RunWith(classOf[JUnitRunner])
class MenuServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "MenuService" should {

    "add a new Product to Menu" in memDB {
      MenuService.addToMenu("Fungi", 0.26, "cm", "Pizza") must be_!==(None)
    }

    "add a new Category to Menu" in memDB {
      val newCategory = Menu(303, "", 0.0, "Stk", "Alkohol", false, true)
      MenuService.addCategory("Alkohol", "Stk") must be_==(newCategory)
    }

    "update a Product in Menu" in memDB {
      MenuService.updateInMenu(101, "Spezi", 0.3, true)
      var product: String = ""
      for (p <- MenuService.listOfAllProducts) {
        if (p.id == 101) {
          product = p.name
        }
      }
      product must be equalTo "Spezi"
    }

    "delete a Product From Menu" in memDB {
      MenuService.rmFromMenu(101) must beTrue
    }

    "delete a not existing Product From Menu" in memDB {
      MenuService.rmFromMenu(-22) must beFalse
    }

    "delete a Category From Menu" in memDB {
      MenuService.rmCategory("Pizza") must beTrue
    }

    "delete a not existing Category From Menu" in memDB {
      MenuService.rmCategory("Shampo") must beFalse
    }

    "edit Category" in memDB {
      MenuService.editCategory("Pizza", "FingerFood")
      var categoryExist: Boolean = false
      for (p <- MenuService.listOfAllProducts) {
        if (p.category.equals("FingerFood")) {
          categoryExist = true
        }
      }
      categoryExist must beTrue
    }

    "edit Category with Result False" in memDB {
      MenuService.editCategory("Pizza", "Fish")
      var categoryExist: Boolean = false
      for (p <- MenuService.listOfAllProducts) {
        if (p.category.equals("FingerFood")) {
          categoryExist = true
        }
      }
      categoryExist must beFalse
    }

    "return length of existing Menu" in memDB {
      MenuService.listOfAllProducts.length must be equalTo 6
    }

    "return length of existing Menu after adding new Product to Menu" in memDB {
      MenuService.addToMenu("Salami", 0.26, "cm", "Pizza")
      MenuService.listOfAllProducts.length must be equalTo 7
    }

    "return length of existing Menu after deleting existing Product from Menu" in memDB {
      MenuService.rmFromMenu(101)
      MenuService.listOfAllProducts.length must be equalTo 5
    }

    "set Product ordered" in memDB {
      var categoryOrdered: Boolean = false
      MenuService.setProductOrdered(List(101))
      for (p <- MenuService.listOfAllProducts) {
        if (p.id == 101) {
          if (p.ordered) {
            categoryOrdered = true
          }
        }
      }
      categoryOrdered must beTrue
    }

    "return List of editable Products" in memDB {
      MenuService.listOfEditableProducts.length must be equalTo 6
    }

    "return List of editable Products after adding a new Product" in memDB {
      MenuService.addToMenu("Schinken", 0.26, "cm", "Pizza")
      MenuService.listOfEditableProducts.length must be equalTo 7
    }

    "return List of editable Products after deleting an existing Product" in memDB {
      MenuService.rmFromMenu(101)
      MenuService.listOfEditableProducts.length must be equalTo 5
    }

    "return List of orderable Products" in memDB {
      MenuService.listOfOrderableProducts.length must be equalTo 6
    }

    "return List of orderable Products after adding a new Product" in memDB {
      MenuService.addToMenu("Schinken", 0.26, "cm", "Pizza")
      MenuService.listOfEditableProducts.length must be equalTo 7
    }

    "return List of orderable Products after deleting an existing Product" in memDB {
      MenuService.rmFromMenu(101)
      MenuService.listOfEditableProducts.length must be equalTo 5
    }

    "return List of addable Categories" in memDB {
      MenuService.listOfAddableCategories.length must be equalTo 3
    }

    "return List of addable Categories after added new Category" in memDB {
      MenuService.addCategory("Alkohol", "Stk")
      MenuService.listOfAddableCategories.length must be equalTo 4
    }

    "return List of addable Categories after deleting existing Category" in memDB {
      MenuService.rmCategory("Pizza")
      MenuService.listOfAddableCategories.length must be equalTo 2
    }

    "return List of orderable Categories" in memDB {
      MenuService.listOfOrderableCategories.length must be equalTo 3
    }

    "return List of orderable Categories after adding a new Category and Product" in memDB {
      MenuService.addCategory("Alkohol", "Stk")
      MenuService.addToMenu("Bier", 4.26, "Stk", "Alkohol")
      MenuService.listOfOrderableCategories.length must be equalTo 4
    }
  }
}
