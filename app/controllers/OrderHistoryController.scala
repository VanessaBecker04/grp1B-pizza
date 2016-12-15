package controllers
import play.api.mvc.{Action, AnyContent, Controller}
import services.OrderHistoryService

/**
  * Created by Hasi on 14.12.2016.
  */
object OrderHistoryController extends Controller {

  def addToHistory(): Action[AnyContent] = Action {
    services.OrderHistoryService.addToHistory(models.OrderProcess.orderID, models.OrderProcess.customerID, models.OrderProcess.customerData, models.OrderProcess.orderedProducts.toString(), models.OrderProcess.sumOfOrder, models.OrderProcess.orderDate)
    Redirect(routes.UserController.attemptSuccessful)
  }

  def showOrdersUser(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersUser(OrderHistoryService.showOrdersUser(models.activeUser.id)))
  }

  def showOrdersEmployee(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersEmployee(OrderHistoryService.showOrdersEmployee))
  }
}
