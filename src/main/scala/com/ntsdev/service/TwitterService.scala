package com.ntsdev.service

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, User, Users}
import com.ntsdev.config.EnvironmentConfig
import com.ntsdev.domain.Person

import scala.collection.JavaConversions
import scala.concurrent.{ExecutionContext, Future}

class TwitterService(implicit executionContext: ExecutionContext) extends EnvironmentConfig {

  private val consumerToken = ConsumerToken(consumerKey, consumerSecret)

  private def buildClient(accessToken: String = defaultAccessTokenKey) = {
    val userToken = AccessToken(accessToken, defaultAccessSecret)
    new TwitterRestClient(consumerToken, userToken)
  }

  def getContacts(accessToken: String): Future[Seq[Person]] = {
    usersToPersons(getFriends(accessToken))
  }

  def getFriends(accessToken: String): Future[Users] = {
    val client = buildClient()
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




}
