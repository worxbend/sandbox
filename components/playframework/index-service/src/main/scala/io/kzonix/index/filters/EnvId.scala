package io.kzonix.index.filters

import com.typesafe.config.Config
import play.api.ConfigLoader

import scala.util.Random

case class EnvId(id: String)

object EnvId {

  implicit val configLoader: ConfigLoader[EnvId] = (config: Config, path: String) => {
    var id: String = config.getString(path)
    id = if (id == null) new Random().nextString(24) else id
    EnvId(id)
  }

}
