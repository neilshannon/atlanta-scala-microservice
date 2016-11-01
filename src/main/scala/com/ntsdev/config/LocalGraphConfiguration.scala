package com.ntsdev.config

import com.ntsdev.neo4j.Neo4jConnectivity
import org.neo4j.ogm.session.{Session, SessionFactory}
import org.slf4j.LoggerFactory
import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Profile(Array("default"))
@Configuration
@EnableTransactionManagement
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@ComponentScan(basePackages = Array("com.ntsdev.repository", "com.ntsdev.service"))
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

}

object LocalGraphConfiguration extends LocalGraphConfiguration