package models

/**
  * Created by Hasi on 18.12.2016.
  */
object DeliveryTime {
  var expectedTime: Int = _
  var customerName: String = _
}

case class calculateDeliveryTime(zip: Int, name: String) {
  val customerZip = zip
  DeliveryTime.customerName = name

  var t: Int = _

  if (customerZip == 82343) {
    t = 6 / 2
  } else if (customerZip == 82340) {
    t = 10 / 2
  } else if (customerZip == 82346) {
    t = 16 / 2
  } else if (customerZip == 82327) {
    t = 16 / 2
  } else if (customerZip == 82229) {
    t = 16 / 2
  } else if (customerZip == 82234) {
    t = 14 / 2
  } else if (customerZip == 82131) {
    t = 12 / 2
  } else if (customerZip == 82061) {
    t = 12 / 2
  } else if (customerZip == 82065) {
    t = 16 / 2
  } else if (customerZip == 82069) {
    t = 12 / 2
  } else if (customerZip == 82335) {
    t = 6 / 2
  } else if (customerZip == 82057) {
    t = 16 / 2
  } else if (customerZip == Company.zip) {
    t = 2
  } else {
  }
  if (t > 0) {
    DeliveryTime.expectedTime = t + 10
  } else {
    DeliveryTime.expectedTime = -1
  }
}