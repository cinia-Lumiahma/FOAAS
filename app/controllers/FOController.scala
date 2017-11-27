package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import models.{FOModelImplicits, JsonInfo}
import play.api.{Configuration, Logger}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.FOService

import scala.concurrent.{ExecutionContext, Future}

class FOController @Inject()(cc: ControllerComponents,
                             actorSystem: ActorSystem,
                             fos: FOService,
                             conf: Configuration)(implicit exec: ExecutionContext) extends AbstractController(cc) with FOModelImplicits {

  def version(callback: Option[String]): Action[AnyContent] = Action.async { request =>

    val retValue = Json.toJson(JsonInfo(fos.getVersion))

    (
      if(callback.nonEmpty){
        Future.successful(Ok(s"${callback.get}(${Json.stringify(retValue)});"))
      }else{
        Future.successful(Ok(retValue))
      }
    )recoverWith{
      case ex: Exception =>

        val errorMsg = s"FOController.version: Exception caught: ${ex.printStackTrace}"
        Logger.error(errorMsg)
        Future.successful(InternalServerError(errorMsg))
    }
  }

}
