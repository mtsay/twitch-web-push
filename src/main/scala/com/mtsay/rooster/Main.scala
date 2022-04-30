package com.mtsay.rooster

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._

class RoosterConfig(val base: Config) {
  private val config = base.getConfig("rooster")

  val interface = config.getString("interface")
  val port = config.getInt("port")
}

object RoosterConfig {
  def apply() = {
    new RoosterConfig(ConfigFactory.load())
  }
}

object Main extends App {
  val config = RoosterConfig()

  implicit val actorSystem = ActorSystem("rooster", config.base)
  implicit val executionContext = actorSystem.dispatcher

  val routes =
    path("healthcheck") {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ok"))
      }
    } ~ path("shutdown") {
      get {
        terminate()

        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "ok"))
      }
    }

  val bindingFuture =
    Http().newServerAt(config.interface, config.port).bind(routes)

  def terminate(): Unit = {
    bindingFuture.map(_.addToCoordinatedShutdown(10.seconds))
      .onComplete(_ => actorSystem.terminate())
  }
}
