package com.ntsdev.http.routes

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives
import com.danielasfregola.twitter4s.entities.AccessToken
import com.danielasfregola.twitter4s.http.unmarshalling.{JsonSupport => Twitter4sJsonSupport}
import com.ntsdev.http.marshalling.JsonSupport
import com.ntsdev.service.TwitterService

import scala.concurrent.ExecutionContext

class TwitterRoutes(var twitterService: TwitterService)(implicit executionContext: ExecutionContext) extends Directives with Twitter4sJsonSupport with JsonSupport {
  val route = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("contacts") {
          val token: AccessToken = twitterService.getAccessToken //TODO replace with real access token
          val contacts = twitterService.getContacts(token)
          complete(contacts)
        } ~
        pathPrefix("twitter") {
          path("authorize"){
            val requestToken = twitterService.getRequestToken
              redirect(Uri(s"https://api.twitter.com/oauth/authorize?oauth_token=${requestToken.getToken}"), StatusCodes.Found)
            }
          } ~
          path("oauthcallback") {
            entity(as[Map[String, String]]) { requestParams =>
              val requestToken = requestParams("oauth_token")
              redirect(Uri(s"https://api.twitter.com/oauth/authorize?oauth_token=$requestToken"), StatusCodes.Found)
            }
          }
        }
      }
    }

}
