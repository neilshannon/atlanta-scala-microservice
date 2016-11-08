package com.ntsdev.config

import com.ntsdev.service.TestDataService
import org.neo4j.ogm.session.SessionFactory
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
    classOf[com.ntsdev.domain.Person],
    classOf[com.ntsdev.service.TestDataService],
    classOf[com.ntsdev.service.AtlantaScalaMicroservice]
  )
)
@Primary
class LocalGraphConfiguration {
  val log = LoggerFactory.getLogger(getClass)

  @Bean
  @Primary
  def getNeo4jConfig: org.neo4j.ogm.config.Configuration = {
    val config = new org.neo4j.ogm.config.Configuration()
    config
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
      .setEncryptionLevel("NONE")

    config
  }

  @Bean
  def getSessionFactory(neo4jConfig: org.neo4j.ogm.config.Configuration): SessionFactory = {
    new SessionFactory(neo4jConfig, "com.ntsdev.domain"){}
  }

  @Bean
  def transactionManager(sessionFactory: SessionFactory): Neo4jTransactionManager = {
    new Neo4jTransactionManager(sessionFactory)
  }

  @Bean
  def testDataService(): TestDataService = {
    new TestDataService()
  }

}

object LocalGraphConfiguration extends LocalGraphConfiguration