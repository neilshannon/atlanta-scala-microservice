package com.ntsdev.config

import com.ntsdev.repository.PersonRepository
import com.typesafe.config.ConfigFactory
import org.neo4j.ogm.config.{Configuration => NeoConfig}
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Profile(Array("cloud"))
@Configuration
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@EnableTransactionManagement
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.service.AtlantaScalaMicroservice])
)
@Primary
class RemoteGraphConfiguration {
  private val log = LoggerFactory.getLogger(getClass)

  @(Autowired)
  val personRepository: PersonRepository = null

  @Bean
  @Primary
  def getNeo4jConfig: org.neo4j.ogm.config.Configuration = {
    val config = ConfigFactory.systemEnvironment().withFallback(ConfigFactory.load())

    val neo4jConfig = config.getConfig("neo4j")
    val neo4jHost = neo4jConfig.getString("host")
    val neo4jPort = neo4jConfig.getInt("boltPort")
    val neo4jUser = neo4jConfig.getString("username")
    val neo4jPass = neo4jConfig.getString("password")

    val URI = s"bolt://$neo4jUser:$neo4jPass@$neo4jHost:$neo4jPort"
    buildGraphConfig(URI)

  }

  @Bean
  def getSessionFactory: SessionFactory = {
    new SessionFactory(getNeo4jConfig, "com.ntsdev.domain")
  }

  @Bean
  def transactionManager(): Neo4jTransactionManager = {
    new Neo4jTransactionManager(getSessionFactory)
  }

  private def buildGraphConfig(URI: String): NeoConfig = {
    val graphConfig: NeoConfig = new NeoConfig()
    graphConfig
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
      .setURI(URI)
      .setEncryptionLevel("NONE")
      .setConnectionPoolSize(150)

    graphConfig
  }

}

object RemoteGraphConfiguration extends RemoteGraphConfiguration



