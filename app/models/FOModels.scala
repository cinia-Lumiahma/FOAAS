package models

import play.api.libs.json.Json

case class JsonInfo(msg: String)

case class JsonError(err: String)

trait FOModelImplicits {

  implicit val infoFormat = Json.format[JsonInfo]
  implicit val errorFormat = Json.format[JsonError]

}
