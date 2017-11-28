package services

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import com.google.inject.Inject
import models.{FOModelImplicits, JsonInfo}
import play.api.libs.json.{JsValue, Json}
import play.api.{Configuration, Logger}
import play.api.mvc.AnyContent

import scala.xml.Elem

class FOService @Inject()(conf: Configuration) extends FOModelImplicits {

  def RET_JSON = "application/json"
  def RET_TEXT = "text/plain"
  def RET_HTML = "text/html"
  def RET_XML = "application/xml"

  def getVersion: String = conf.getOptional[String]("app.version").getOrElse("0.0")

  def formJsonReturnValue(message: String, subtitle: Option[String]): JsValue = Json.toJson(JsonInfo(message, subtitle))

  def formJsonpReturnValue(message: String, subtitle: Option[String], callback: String): String = s"$callback(${Json.toJson(JsonInfo(message, subtitle))});"

  def formTextReturnValue(message: String, subtitle: Option[String]): String =
    if(subtitle.nonEmpty){
      s"$message ${subtitle.getOrElse("")}"
    }else{
      s"$message"
    }
  def formXmlReturnValue(message: String, subtitle: Option[String] = None): Elem = <message status="OK">{message}</message>

  def formHtmlReturnValue(message: String, subtitle: Option[String] = None) = "herp"

}
