package controllers

import forms.{CreateProductForm, LongForm, EditMenuForm, CreateCategoryForm, EditCategoryForm, StringForm}
import play.api.data.Form
import play.api.data.Forms.{mapping, text, of, longNumber}
import play.api.data.format.Formats.{doubleFormat, booleanFormat}
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

/** Controller for the menu.
  *
  * @author Maximilian Öttl, Hasibullah Faroq
  */
object MenuController extends Controller {

  /** Form object for the user data */
  val menuForm = Form(
    mapping(
      "Produktname" -> text.verifying("Bitte einen Produktnamen eingeben", !_.isEmpty),
      "Preis je Einheit" -> of[Double],
      "Kategorie" -> text.verifying("Bitte einen Produktnamen eingeben", !_.isEmpty))
    (CreateProductForm.apply)(CreateProductForm.unapply))
  val rmForm = Form(
    mapping(
      "Id" -> longNumber)
    (LongForm.apply)(LongForm.unapply))
  val updateForm = Form(
    mapping(
      "Id" -> longNumber,
      "Neuer Name" -> text.verifying("Bitte neuen Namen für das Produkt eingeben", !_.isEmpty),
      "Neuer Preis" -> of[Double],
      "Aktivieren" -> of[Boolean])
  (EditMenuForm.apply)(EditMenuForm.unapply))
  val addCategoryForm = Form(
    mapping(
      "Name" -> text,
      "Maßeinheit" -> text)(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  )
  val editCategoryForm = Form(
    mapping(
      "Alter Name" -> text,
      "Neuer Name" -> text)
    (EditCategoryForm.apply)(EditCategoryForm.unapply))
  val rmCategoryForm = Form(
    mapping(
      "Kategorie" -> text)
    (StringForm.apply)(StringForm.unapply))

  /** Add a new product to the menu.
    *
    * @return editMenu
    */

  def addToMenu(): Action[AnyContent] = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        var unit: String = ""
        if (!MenuService.listOfAllProducts.exists(p => p.name == userData.name && p.category == userData.category)) {
          for (p <- services.MenuService.listOfAllProducts) {
            if (p.category.equals(userData.category)) {
              unit = p.unit
            }
          }
          services.MenuService.addToMenu(userData.name, userData.price, unit, userData.category)
          Redirect(routes.MenuController.editMenu())
        } else {
          Redirect(routes.UserController.attemptFailed("productDoesExist"))
        }
      })
  }

  /** Adds a new category to the menu.
    *
    * @return editCategory
    */
  def addCategory(): Action[AnyContent] = Action { implicit request =>
    addCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        if (!MenuService.listOfOrderableCategories.exists(c => c.category == userData.name)) {
          services.MenuService.addCategory(userData.name, userData.unit)
          Redirect(routes.MenuController.editCategory())
        } else {
          Redirect(routes.UserController.attemptFailed("categoryused"))
        }
      })
  }

  /** Updates information of a product.
    *
    * @return editMenu
    */
  def updateInMenu(): Action[AnyContent] = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        services.MenuService.updateInMenu(userData.id, userData.name, userData.price, userData.active)
        Redirect(routes.MenuController.editMenu())
      })
  }

  /** Updates information of a category.
    *
    * @return editCategory
    */

  def updateCategory(): Action[AnyContent] = Action { implicit request =>
    editCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        services.MenuService.editCategory(userData.oldCategory, userData.newCategory)
        Redirect(routes.MenuController.editCategory())
      })
  }

  /** Deletes a category and deletes/disables any products within the specified category.
    *
    * @return editCategory
    */
  def rmCategory: Action[AnyContent] = Action { implicit request =>
    rmCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        services.MenuService.rmCategory(userData.value)
        Redirect(routes.MenuController.editCategory())
      })
  }

  /** Products which haven't been ordered yet will be deleted.
    * Products which have been ordered will be deactivated.
    *
    * @return editMenu
    */
  def rmFromMenu: Action[AnyContent] = Action { implicit request =>
    rmForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.attemptFailed("badRequest"))
      },
      userData => {
        services.MenuService.rmFromMenu(userData.value)
        Redirect(routes.MenuController.editMenu())
      })
  }

  /** Shows the editMenu view to edit the menu/products.
    *
    * @return editMenu
    */
  def editMenu: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editMenu(MenuService.listOfEditableProducts, MenuService.listOfAddableCategories, menuForm, rmForm, updateForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Shows the editCategory view to edit a category.
    *
    * @return editCategory
    */
  def editCategory: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editCategory(MenuService.listOfAddableCategories, MenuService.listOfAllCategories, addCategoryForm, editCategoryForm, rmCategoryForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Shows the showMenu view.
    *
    * @return showMenu
    */
  def showMenu: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.showMenu(MenuService.listOfOrderableProducts, MenuService.listOfOrderableCategories, controllers.BillController.billform))
  }
}