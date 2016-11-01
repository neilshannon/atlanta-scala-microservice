package com.ntsdev.neo4j

import com.ntsdev.config.ServiceConfig

trait Neo4jConnectivity extends ServiceConfig {
  val neo4jHost = config.getString("services.neo4j.host")
  val neo4jPort = config.getInt("services.neo4j.boltPort")
  val neo4jUser = config.getString("services.neo4j.username")
  val neo4jPass = config.getString("services.neo4j.password")
}
