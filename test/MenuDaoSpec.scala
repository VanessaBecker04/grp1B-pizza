/**
  * Created by Hasib on 04.06.2017.
  */

import dbaccess.MenuDao
import models.Menu
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.FakeApplication
import play.api.test.Helpers.running

@RunWith(classOf[JUnitRunner])
class MenuDaoSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "MenuDao" should {

    "add a new Product to Menu" in memDB {
      MenuDao.addToMenu(Menu(-1, "Fungi", 0.26, "cm", "Pizza", false, true)) must be_!==(None)
    }

    "add a new Category to Menu" in memDB {
      val newCategory = Menu(-1, "", 0, "cm", "Pizza", false, true)
      MenuDao.addCategoryToMenu(newCategory) must be_==(newCategory)
    }

    "update a Product in Menu" in memDB {
      MenuDao.updateInMenu(101, "Spezi", 0.3, true)
      var product: String = ""
      for (p <- MenuDao.listOfProducts) {
        if (p.id == 101) {
          product = p.name
        }
      }
      product must be equalTo "Spezi"
    }

    "delete a Product From Menu" in memDB {
      MenuDao.rmFromMenu(101) must beTrue
    }

    "delete a not existing Product From Menu" in memDB {
      MenuDao.rmFromMenu(-22) must beFalse
    }

    "delete a Category From Menu" in memDB {
      MenuDao.rmCategory("Pizza") must beTrue
    }

    "delete a not existing Category From Menu" in memDB {
      MenuDao.rmCategory("Shampo") must beFalse
    }

    "edit Category" in memDB {
      MenuDao.editCategory("Pizza", "FingerFood")
      var categoryExist: Boolean = false
      for (p <- MenuDao.listOfProducts) {
        if (p.category.equals("FingerFood")) {
          categoryExist = true
        }
      }
      categoryExist must beTrue
    }

    "edit Category with Result False" in memDB {
      MenuDao.editCategory("Pizza", "Fish")
      var categoryExist: Boolean = false
      for (p <- MenuDao.listOfProducts) {
        if (p.category.equals("FingerFood")) {
          categoryExist = true
        }
      }
      categoryExist must beFalse
    }

    "return length of existing Menu" in memDB {
      MenuDao.listOfProducts.length must be equalTo 6
    }

    "set Product ordered" in memDB {
      var categoryOrdered: Boolean = false
      MenuDao.setProductOrdered(List(101))
      for (p <- MenuDao.listOfProducts) {
        if (p.id == 101) {
          if (p.ordered) {
            categoryOrdered = true
          }
        }
      }
      categoryOrdered must beTrue
    }
  }
}
