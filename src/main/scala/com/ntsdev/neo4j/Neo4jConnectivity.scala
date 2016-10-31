package com.ntsdev.neo4j

import com.ntsdev.ServiceConfig
import org.slf4j.LoggerFactory
//import org.anormcypher._
//import play.api.libs.ws._

trait Neo4jConnectivity extends ServiceConfig {
  //implicit val wsclient = ning.NingWSClient()

  val neo4jHost = config.getString("services.neo4j.host")
  val neo4jPort = config.getInt("services.neo4j.webPort")
  val neo4jUser = config.getString("services.neo4j.username")
  val neo4jPass = config.getString("services.neo4j.password")

  val logger = LoggerFactory.getLogger(getClass)

  logger.info(s"Bootstrapping neo4j connection to host [$neo4jHost:$neo4jPort]")

  //implicit val connection: Neo4jConnection = Neo4jREST(neo4jHost, neo4jPort, neo4jUser, neo4jPass)
}
