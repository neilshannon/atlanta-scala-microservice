package com.ntsdev.config

import com.ntsdev.neo4j.Neo4jConnectivity
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{Bean, Profile}
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableAutoConfiguration
@Configuration
@Profile(Array("!cloud"))
@EnableNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@EnableTransactionManagement
class LocalGraphConfiguration extends Neo4jConfiguration with Neo4jConnectivity {
  private val log = LoggerFactory.getLogger(getClass)

  @Bean
  def getLocalConfiguration: Configuration = {
    val config = new Configuration()
    config
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")

    log.info("Bootstrapping local graph configuration")

    config
  }

  @Override
  def getSessionFactory: SessionFactory = {
    new SessionFactory(getLocalConfiguration, "com.ntsdev.domain")
  }
}

object LocalGraphConfiguration extends LocalGraphConfiguration