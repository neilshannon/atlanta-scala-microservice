package com.ntsdev.http.routes

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives
import com.danielasfregola.twitter4s.entities.AccessToken
import com.danielasfregola.twitter4s.http.unmarshalling.{JsonSupport => Twitter4sJsonSupport}
import com.github.scribejava.core.model.OAuth1RequestToken
import com.ntsdev.config.EnvironmentConfig
import com.ntsdev.http.marshalling.JsonSupport
import com.ntsdev.service.TwitterService
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionOptions._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

class TwitterRoutes(var twitterService: TwitterService)
                   (implicit executionContext: ExecutionContext, sessionManager: SessionManager[Map[String, String]])
                    extends Directives with Twitter4sJsonSupport with JsonSupport {

  private val log = LoggerFactory.getLogger(getClass)

  val route = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("contacts") {
          requiredSession(oneOff, usingCookies) { session =>
            val accessKey = session("accessToken")
            val accessSecret = session("accessSecret")
            val token = AccessToken(accessKey, accessSecret)
            val contacts = twitterService.getContacts(token)
            complete(contacts)
          }
        } ~
        pathPrefix("twitter") {
          path("authorize") {
            val requestToken = twitterService.getRequestToken
            val requestTokenAndSecret = Map("requestToken" -> requestToken.getToken, "requestSecret" -> requestToken.getTokenSecret)
            setSession(oneOff, usingCookies, requestTokenAndSecret) {
              redirect(Uri(s"https://api.twitter.com/oauth/authorize?oauth_token=${requestToken.getToken}"), StatusCodes.Found)
            }
          } ~
          path("oauthcallback") {
            parameters('oauth_token, 'oauth_verifier) { (oauth_token, oauth_verifier) =>
              requiredSession(oneOff, usingCookies){ session =>
                val tokenSecret: String = session("requestToken")
                val oauthRequestToken = new OAuth1RequestToken(oauth_token, tokenSecret)
                val accessToken = twitterService.getAccessToken(oauthRequestToken, oauth_verifier)
                val accessTokenAndSecret = Map("accessToken" -> accessToken.key, "accessSecret" -> accessToken.secret)
                setSession(oneOff, usingCookies, accessTokenAndSecret) {
                  log.debug(s"BaseUrl: [${EnvironmentConfig.baseUrl}]")
                  redirect(Uri(s"${EnvironmentConfig.baseUrl}/contacts"), StatusCodes.Found)
                }
              }
            }
          }
        }
      }
    }
  }
}
