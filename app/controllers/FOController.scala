package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import models.{FOModelImplicits, JsonInfo}
import play.api.{Configuration, Logger}
import play.api.libs.json.Json
import play.api.mvc._
import services.FOService

import scala.concurrent.{ExecutionContext, Future}

class FOController @Inject()(cc: ControllerComponents,
                             fos: FOService,
                             conf: Configuration)(implicit exec: ExecutionContext) extends AbstractController(cc) with FOModelImplicits {

  def version(callback: Option[String]): Action[AnyContent] = Action.async { request =>

    val retValue = Json.toJson(JsonInfo(fos.getVersion))

    (

      if(callback.nonEmpty) //JSONP return value
        Future.successful(Ok(s"${callback.get}(${Json.stringify(retValue)});"))
      else
        Future.successful(Ok(retValue))

    )recoverWith{

      case ex: Exception =>
        val errorMsg = s"FOController.version: Exception caught: ${ex.printStackTrace}"
        Logger.error(errorMsg)
        Future.successful(InternalServerError(errorMsg))

    }
  }

  def asshole(from: String, callback: Option[String]): Action[AnyContent] = Action.async{ request =>

    formResponse(
      request.headers.get("Accept"),
      "Fuck off, asshole.",
      Some(s"from: $from"),
      callback
    ) recoverWith{
      case ex: Exception =>
        val errorMsg = s"FOController.version: Exception caught: ${ex.printStackTrace}"
        Logger.error(errorMsg)
        Future.successful(InternalServerError(errorMsg))
    }

  }

  private def formResponse(acceptHeader: Option[String] = None, message: String, subtitle: Option[String] = None, callback: Option[String] = None): Future[Result] = {

    val aHeader = acceptHeader.getOrElse(fos.RET_JSON)

    if(aHeader.equals(fos.RET_JSON)){

      if(callback.nonEmpty){
        Future.successful(Ok(fos.formJsonpReturnValue(message, subtitle, callback.getOrElse("eval"))))
      }else{
        Future.successful(Ok(fos.formJsonReturnValue(message, subtitle)))
      }

    }else if(aHeader.equals(fos.RET_TEXT)){

      Future.successful(Ok(fos.formTextReturnValue(message, subtitle)))

    }else if(aHeader.equals(fos.RET_XML)){

      Future.successful(Ok(fos.formXmlReturnValue(message, subtitle)))

    }else if (aHeader.equals(fos.RET_HTML)){

      Future.successful(Ok(fos.formHtmlReturnValue(message, subtitle)))

    }else{
      Logger.warn(s"Unidentified Accept-header value detected ($aHeader), defaulting to JSON")
      Future.successful(Ok(fos.formJsonReturnValue(message, subtitle)))
    }
  }

}
