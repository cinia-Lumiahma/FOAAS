package services

import com.google.inject.Inject
import play.api.Configuration

class FOService @Inject()(conf: Configuration) {

  def getVersion: String = conf.getOptional[String]("app.version").getOrElse("0.0")


}
