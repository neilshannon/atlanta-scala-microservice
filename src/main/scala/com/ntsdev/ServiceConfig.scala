package com.ntsdev

import com.typesafe.config.ConfigFactory

trait ServiceConfig {
  val systemEnvironment = ConfigFactory.systemEnvironment()
  val config = ConfigFactory.load().withFallback(systemEnvironment)
}
