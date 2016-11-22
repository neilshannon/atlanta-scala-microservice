package com.ntsdev.config

import com.typesafe.config.{Config, ConfigFactory}

trait EnvironmentConfig {
  val systemEnvironment = ConfigFactory.systemEnvironment()
  var config = systemEnvironment.withFallback(ConfigFactory.load())

  val neo4jConfig = config.getConfig("neo4j")
  val neo4jHost = neo4jConfig.getString("host")
  val neo4jPort = neo4jConfig.getInt("boltPort")
  val neo4jUser = neo4jConfig.getString("username")
  val neo4jPass = neo4jConfig.getString("password")

  val activeSpringProfile = config.getString("spring.profiles.active")

  val interface = config.getString("http.interface")
  val port = config.getInt("http.port")

  val twitterConfig = config.getConfig("twitter")
  val consumerKey = twitterConfig.getString("consumer.key")
  val consumerSecret = twitterConfig.getString("consumer.secret")
  val defaultAccessTokenKey = twitterConfig.getString("access.key")
  val defaultAccessSecret = twitterConfig.getString("access.secret")
}

object EnvironmentConfig extends EnvironmentConfig {
  def baseUrl: String = {
    dynamicConfig.getString("http.baseurl")
  }

  def callbackUrl: String = {
    dynamicConfig.getString("twitter.callbackurl")
  }

  private def dynamicConfig: Config = {
    ConfigFactory.systemEnvironment().withFallback(ConfigFactory.load())
  }
}
