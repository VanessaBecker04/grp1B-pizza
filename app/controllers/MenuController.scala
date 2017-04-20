package controllers

import forms.{CreateMenuForm, CreateRemoveFromMenuForm, CreateUpdateInMenuForm}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.data.validation.Constraints.min
import play.api.mvc.{Action, AnyContent, Controller}
import services.MenuService

import scala.collection.mutable.ListBuffer

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
      "Maßeinheit" -> text,
      "Kategorie" -> text.verifying("Bitte einen Produktnamen eingeben", !_.isEmpty))
    (CreateMenuForm.apply)(CreateMenuForm.unapply))
  val rmForm = Form(mapping("Id" -> longNumber)(CreateRemoveFromMenuForm.apply)(CreateRemoveFromMenuForm.unapply))
  val updateForm = Form(
    mapping(
      "Id" -> longNumber,
      "Neuer Name" -> text.verifying("Bitte neuen Namen für das Produkt eingeben", !_.isEmpty),
      "Neuer Preis" -> of[Double],
      "Aktivieren" -> of[Boolean])
  (CreateUpdateInMenuForm.apply)(CreateUpdateInMenuForm.unapply))

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
        for (p <- services.MenuService.addedToMenu) {
          if (p.name.equals(userData.name) && p.category.equals(userData.category)) {
            nameExist = true
          }
        }
        if (nameExist) {
          Redirect(routes.UserController.attemptFailed("productDoesExist"))
        } else {
          val success = services.MenuService.addToMenu(userData.name, userData.price, userData.unitOfMeasurement, userData.category)
          if (success != null) {
            Redirect(routes.MenuController.editMenu())
          } else {
            Redirect(routes.UserController.attemptFailed("numberOfCategories"))
          }
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

  /** Löscht ein Produkt komplett von der Speisekarte, jedoch nur wenn zuvor dieser nicht schon einmal bestellt wurde.
    * Falls jedoch schon mal bestellt wurde wird diese bloß deaktiviert, somit verschwindet sie aus der Bestellübersicht
    * aber nicht aus der Datenbank Menu.
    *
    * @return editMenu(Editor für die Speisekarte)
    */
  def rmFromMenu: Action[AnyContent] = Action { implicit request =>
    rmForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.editMenu(null, formWithErrors, null))
      },
      userData => {
        for (k <- services.MenuService.addedToMenu) {
          if (k.id == userData.id && !k.ordered) {
            services.MenuService.rmFromMenu(userData.id)
          } else if (k.id == userData.id && k.ordered) {
            services.MenuService.setProductInactive(userData.id)
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
      MenuService.putAllMenuIDInList()
      Ok(views.html.editMenu(controllers.MenuController.menuForm, controllers.MenuController.rmForm, controllers.MenuController.updateForm))
    } else {
      Ok(views.html.attemptFailed("permissiondenied"))
    }
  }

  /** Zeigt die Speisekarte an.
    *
    * @return showMenu
    */
  def showMenu: Action[AnyContent] = Action { implicit request =>
    MenuService.categorize()
    Ok(views.html.showMenu(MenuService.addedToMenu, controllers.BillController.billform))
  }

}
