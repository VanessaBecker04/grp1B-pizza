package models

/** Lieferzeit Objekt, welches Lieferzeit und weitere Variablen verwaltete die für die Berechnung der Lieferzeit benötigt wird.
  * Created by Hasibullah Faroq on 18.12.2016.
  */
object DeliveryTime {
  var expectedTime: Int = _
  var customerName: String = _
  var kilometersperminute: Int = 1
  var bakeTime: Int = 10
}

/**Berechnung der Lieferzeit.
  *
  * @param zip Postleitzeil des Kunden
  * @param name Name des Kunden
  */
case class calculateDeliveryTime(zip: Int, name: String) {
  val customerZip = zip
  DeliveryTime.customerName = name
  var km: Int = _
  var kmpm: Int = DeliveryTime.kilometersperminute

  customerZip match {
    case Company.zip => km = 4
    case 82335 | 82343 => km = 6
    case 82340 => km = 10 / kmpm
    case 82061 | 82069 | 82131 => km = 12
    case 82234 => km = 14
    case 82057 | 82065 | 82229 | 82327 | 82346 => km = 16
    case _ =>
  }
  var t = km / kmpm
  if (t > 0) {
    DeliveryTime.expectedTime = t + DeliveryTime.bakeTime
  } else {
    DeliveryTime.expectedTime = -1
  }
}