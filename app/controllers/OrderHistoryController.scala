package controllers
import play.api.mvc.{Action, AnyContent, Controller}
import services.OrderHistoryService

/**
  * Created by Hasi on 14.12.2016.
  */
object OrderHistoryController extends Controller {

  def addToHistory(): Action[AnyContent] = Action {
    services.OrderHistoryService.addToHistory(models.CustomerOrderProcess.customerId, models.CustomerOrderProcess.customerData, models.CustomerOrderProcess.orderedProducts.toString(), models.CustomerOrderProcess.sumOfOrder, models.CustomerOrderProcess.orderDate)
    Redirect(routes.UserController.attemptSuccessful)
  }

  def showOrdersUser(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersUser(OrderHistoryService.showOrdersUser(models.activeUser.id)))
  }

  def showOrdersEmployee(): Action[AnyContent] = Action {
    Ok(views.html.showOrdersEmployee(OrderHistoryService.showOrdersEmployee))
  }
}
