package com.ntsdev.config

import com.ntsdev.neo4j.Neo4jConnectivity
import com.ntsdev.repository.PersonRepository
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

import scala.annotation.meta.setter

@Profile(Array("cloud"))
@Configuration
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@EnableTransactionManagement
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.service.AtlantaScalaMicroservice])
)
@Primary
class RemoteGraphConfiguration extends Neo4jConnectivity {
  private val log = LoggerFactory.getLogger(getClass)

  @(Autowired @setter)
  var personRepository: PersonRepository = _

  @Bean
  @Primary
  def getNeo4jConfig: org.neo4j.ogm.config.Configuration = {
    val URI = s"bolt://$neo4jUser:$neo4jPass@$neo4jHost:$neo4jPort"

    val config = new org.neo4j.ogm.config.Configuration()
    config
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
      .setURI(URI)
      .setEncryptionLevel("NONE")
      .setConnectionPoolSize(150)
    config
  }

  @Bean
  @Primary
  def getSessionFactory: SessionFactory = {
    new SessionFactory(getNeo4jConfig, "com.ntsdev.domain")
  }

}

object RemoteGraphConfiguration extends RemoteGraphConfiguration



