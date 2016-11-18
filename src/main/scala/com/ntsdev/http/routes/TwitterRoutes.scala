package com.ntsdev.http.routes

import akka.http.scaladsl.server.Directives
import com.ntsdev.service.TwitterService
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, native}

import scala.concurrent.ExecutionContext

class TwitterRoutes(val twitterService: TwitterService)(implicit executionContext: ExecutionContext) extends Directives with Json4sSupport {
  implicit val serialization = native.Serialization
  implicit val jsonFormats: Formats = DefaultFormats

  val route = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("contacts"){
          val contacts = twitterService.getFriends("accessToken")
          complete(contacts)
        }
      }
    }
  }
}
