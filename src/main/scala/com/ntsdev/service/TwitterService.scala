package com.ntsdev.service

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Users}
import com.ntsdev.config.EnvironmentConfig

import scala.concurrent.Future

class TwitterService extends EnvironmentConfig {

  private val consumerToken = ConsumerToken(consumerKey, consumerSecret)

  private def buildClient(accessToken: String = defaultAccessTokenKey) = {
    val userToken = AccessToken(accessToken, defaultAccessSecret)
    new TwitterRestClient(consumerToken, userToken)
  }

  def getFriends(accessToken: String): Future[Users] = {
   buildClient(accessToken).getFriendsForUser("ntshannon")
  }

}
