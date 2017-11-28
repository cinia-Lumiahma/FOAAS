package models

import play.api.libs.json.{Format, JsValue, Json}

case class JsonInfo(message: String, subtitle: Option[String] = None)

case class JsonError(err: String)

trait FOModelImplicits {

  implicit val infoFormat = Json.format[JsonInfo]
  implicit val errorFormat = Json.format[JsonError]

}
