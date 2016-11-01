package com.ntsdev.config

import com.typesafe.config.ConfigFactory

trait ServiceConfig {
  val systemEnvironment = ConfigFactory.systemEnvironment()
  var config = ConfigFactory.load().withFallback(systemEnvironment)
}
