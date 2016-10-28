package com.ntsdev.neo4j

trait Neo4jConnectivity {
  import org.anormcypher._
  import play.api.libs.ws._

  implicit val wsclient = ning.NingWSClient()

  implicit val connection: Neo4jConnection = Neo4jREST("localhost", 7474, "username", "password")
}
