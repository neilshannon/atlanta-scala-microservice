package com.ntsdev.service

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Users}
import com.ntsdev.config.EnvironmentConfig
import org.springframework.stereotype.Service

import scala.concurrent.Future

@Service
class TwitterService extends EnvironmentConfig {

  private val consumerToken = ConsumerToken(consumerKey, consumerSecret)

  private def buildClient(accessToken: String) = {
    val accessToken = AccessToken(accessToken, accessSecret)
    new TwitterRestClient(consumerToken, accessToken)
  }

  private def getRequestToken()

  def getFriends(accessToken: String): Future[Users] = {
   buildClient(accessToken).getFriendsForUser("ntshannon")
  }

}
