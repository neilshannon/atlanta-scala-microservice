package com.ntsdev.http.routes

import akka.http.scaladsl.server.Directives
import com.danielasfregola.twitter4s.http.unmarshalling.{JsonSupport => Twitter4sJsonSupport}
import com.ntsdev.http.marshalling.JsonSupport
import com.ntsdev.service.TwitterService

import scala.concurrent.ExecutionContext

class TwitterRoutes(var twitterService: TwitterService)(implicit executionContext: ExecutionContext) extends Directives with Twitter4sJsonSupport with JsonSupport {
  val route = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("contacts"){
          val contacts = twitterService.getContacts("accessToken")
          complete(contacts)
        }
      }
    }
  }
}
