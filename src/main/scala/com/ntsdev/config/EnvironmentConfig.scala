package com.ntsdev.config

import com.typesafe.config.ConfigFactory

trait EnvironmentConfig {
  private val systemEnvironment = ConfigFactory.systemEnvironment()

  var config = systemEnvironment.withFallback(ConfigFactory.load())

  val neo4jConfig = config.getConfig("neo4j")
  val neo4jHost = neo4jConfig.getString("host")
  val neo4jPort = neo4jConfig.getInt("boltPort")
  val neo4jUser = neo4jConfig.getString("username")
  val neo4jPass = neo4jConfig.getString("password")

  val activeSpringProfile = config.getString("SPRING_PROFILES_ACTIVE")

  val interface = config.getString("http.interface")
  val port = config.getInt("http.port")

  val twitterConfig = config.getConfig("twitter")
  val consumerKey = twitterConfig.getString("consumerKey")
  val consumerSecret = twitterConfig.getString("consumerSecret")
  val accessToken = twitterConfig.getString("accessToken")
  val accessSecret = twitterConfig.getString("accessSecret")

}
