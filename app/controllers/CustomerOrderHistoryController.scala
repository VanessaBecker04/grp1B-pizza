package controllers
import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Created by Hasi on 14.12.2016.
  */
object CustomerOrderHistoryController extends Controller {

  def addToHistory(): Action[AnyContent] = Action {
    services.CustomerOrderHistoryService.addToHistory(models.CustomerOrderProcess.customerId, models.CustomerOrderProcess.customerData, models.CustomerOrderProcess.orderedProducts.toString(), models.CustomerOrderProcess.sumOfOrder, models.CustomerOrderProcess.orderDate)
    Redirect(routes.Application.index())
  }
}
