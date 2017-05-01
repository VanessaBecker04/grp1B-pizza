package controllers

import forms.{CreateProductForm, CreateCategoryForm, LongForm, EditMenuForm, EditCategoryForm, StringForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

/** Kontroller für die Speisekarte, aus welchem der Kunde seine Wunschprodukte auswählen kann und für den Editor der Speisekarte
  * Created by Hasibullah Faroq on 21.11.2016.
  */
object MenuController extends Controller {

  /**
    * Form Objekte für die Benutzer Daten.
    */
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


  /**
    * Fügt ein neues Produkt in die Speisekarte ein.
    *
    * @return editMenu(Editor für die Speisekarte)
    */

  def addToMenu: Action[AnyContent] = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(formWithErrors, null, null))
      },
      userData => {
        var nameExist: Boolean = false
        var unit: String = ""
        for (p <- services.MenuService.listOfProducts) {
          if (p.name.equals(userData.name) && p.category.equals(userData.category)) {
            nameExist = true
          } else {
            unit = p.unit
          }
        }
        if (!MenuService.listOfProducts.exists(p => p.name == userData.name && p.category == userData.category)) {
          services.MenuService.addToMenu(userData.name, userData.price, unit, userData.category)
          Redirect(routes.MenuController.editMenu())
        } else {
          Redirect(routes.UserController.attemptFailed("productDoesExist"))
        }
      })
  }

  def addCategory: Action[AnyContent] = Action { implicit request =>
    addCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editCategory(formWithErrors, null, null))
      },
      userData => {
        if (!MenuService.listOfCategories.exists(c => c.category == userData.name)){
          services.MenuService.addCategory(userData.name, userData.unit)
          Redirect(routes.UserController.attemptSuccessful("categorycreated"))
        } else {
          Redirect(routes.UserController.attemptFailed("categoryused"))
        }
      })
  }

  /** Verändert einzelne Attribute eines schon vorhandenen Produktes in der Speisekarte
    *
    * @return editMenu(Editor für die Speisekarte)
    */
  def updateInMenu: Action[AnyContent] = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, null, formWithErrors))
      },
      userData => {
        services.MenuService.updateInMenu(userData.id, userData.name, userData.price, userData.active)
        Redirect(routes.MenuController.editMenu())
      })
  }

  def updateCategory: Action[AnyContent] = Action { implicit request =>
    editCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editCategory(null, formWithErrors, null))
      },
      userData => {
        services.MenuService.editCategory(userData.oldCategory, userData.newCategory)
        Redirect(routes.MenuController.editCategory())
      })
  }

  /** Löscht ein Produkt komplett von der Speisekarte, jedoch nur wenn zuvor dieser nicht schon einmal bestellt wurde.
    * Falls jedoch schon mal bestellt wurde wird diese bloß deaktiviert, somit verschwindet sie aus der Bestellübersicht
    * aber nicht aus der Datenbank Menu.
    *
    * @return editMenu(Editor für die Speisekarte)
    */
  def rmCategory: Action[AnyContent] = Action { implicit request =>
    rmCategoryForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editCategory(null, null, formWithErrors))
      },
      userData => {
        for (k <- services.MenuService.listOfProducts) {
          if (!k.ordered) {
            services.MenuService.rmCategory(userData.value)
          } else {
            Redirect(routes.MenuController.editCategory())
          }
        }
        Redirect(routes.MenuController.editCategory())
      })
  }

  def rmFromMenu: Action[AnyContent] = Action { implicit request =>
    rmForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, formWithErrors, null))
      },
      userData => {
        for (k <- services.MenuService.listOfProducts) {
          if (k.id == userData.value && !k.ordered) {
            services.MenuService.rmFromMenu(userData.value)
          } else if (k.id == userData.value && k.ordered) {
            services.MenuService.setProductInactive(userData.value)
          } else {
            Redirect(routes.MenuController.editMenu())
          }
        }
        Redirect(routes.MenuController.editMenu())
      })
  }

  /** Zeigt den Editor für die Speisekarte an.
    *
    * @return editMenu
    */
  def editMenu: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editMenu(menuForm, rmForm, updateForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  def editCategory: Action[AnyContent] = Action { implicit request =>
    if (request2session.get("role").get == "Mitarbeiter") {
      Ok(views.html.editCategory(addCategoryForm, editCategoryForm, rmCategoryForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Zeigt die Speisekarte an.
    *
    * @return showMenu
    */
  def showMenu: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.showMenu(MenuService.listOfProducts, MenuService.listOfCategories, controllers.BillController.billform))
  }
}
