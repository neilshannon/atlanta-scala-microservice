package com.ntsdev.service

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, User, Users}
import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import com.ntsdev.config.EnvironmentConfig
import com.ntsdev.domain.Person

import scala.collection.JavaConversions
import scala.concurrent.{ExecutionContext, Future}

class TwitterService(implicit executionContext: ExecutionContext) extends EnvironmentConfig {

  private val consumerToken = ConsumerToken(consumerKey, consumerSecret)

  def requestTokenService: OAuth10aService = {
    new ServiceBuilder()
      .apiKey(consumerKey)
      .apiSecret(consumerSecret)
      .callback(callbackUrl)
      .build(TwitterApi.instance())
  }

  def getAccessToken: AccessToken = {
    //TODO get request token, bind access token in cookie
    AccessToken(defaultAccessTokenKey, defaultAccessSecret)
  }

  def getRequestToken: OAuth1RequestToken = {
    requestTokenService.getRequestToken //todo async
  }

  def getContacts(accessToken: AccessToken): Future[Seq[Person]] = {
    usersToPersons(getFriends(accessToken))
  }

  def getFriends(accessToken: AccessToken): Future[Users] = {
    val client = buildClient
    val usersFuture = client.getFriendsForUser("ntshannon")
    usersFuture
  }

  def usersToPersons(users: Future[Users]): Future[Seq[Person]] = {
    import JavaConversions._
    def userToPerson(user: User): Person = {
      Person(
        id = 0L,
        firstName = user.name,
        lastName = "",
        twitter_id = user.id_str,
        contacts = Set.empty[Person]
      )
    }
    users.map(users => users.users.map(userToPerson))
  }

  private def buildClient = {
    val accessToken = getAccessToken
    new TwitterRestClient(consumerToken, accessToken)
  }

}
