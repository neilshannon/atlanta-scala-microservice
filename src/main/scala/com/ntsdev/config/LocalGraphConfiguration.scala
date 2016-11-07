package com.ntsdev.config

import com.ntsdev.neo4j.Neo4jConnectivity
import com.ntsdev.service.TestDataService
import org.neo4j.ogm.session.{Session, SessionFactory}
import org.slf4j.LoggerFactory
import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Profile(Array("!cloud"))
@Configuration
@EnableTransactionManagement
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.repository.PersonRepository],
    classOf[com.ntsdev.service.TestDataService],
    classOf[com.ntsdev.service.AtlantaScalaMicroservice])
  )
@Primary
class LocalGraphConfiguration extends Neo4jConnectivity {
  val log = LoggerFactory.getLogger(getClass)

  @Bean
  @Primary
  def getConfiguration: org.neo4j.ogm.config.Configuration = {
    val config = new org.neo4j.ogm.config.Configuration()
    config
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
      .setEncryptionLevel("NONE")

    config
  }

  @Bean
  @Primary
  def getSessionFactory: SessionFactory = {
    new SessionFactory(getConfiguration, "com.ntsdev.domain"){}
  }

  @Bean
  @Primary
  def transactionManager(): Neo4jTransactionManager = {
    new Neo4jTransactionManager(getSessionFactory)
  }

  @Bean
  def testDataService(): TestDataService = {
    new TestDataService()
  }

}

object LocalGraphConfiguration extends LocalGraphConfiguration