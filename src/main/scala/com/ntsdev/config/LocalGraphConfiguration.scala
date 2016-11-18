package com.ntsdev.config

import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Profile(Array("!cloud"))
@Configuration
@EnableTransactionManagement
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.domain.Person],
    classOf[com.ntsdev.service.TestDataService],
    classOf[com.ntsdev.service.PersonService],
    classOf[com.ntsdev.service.TwitterService],
    classOf[com.ntsdev.http.AtlantaScalaMicroservice]
  )
)
@Primary
class LocalGraphConfiguration extends GraphConfiguration {

  private final val driverClass = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver"

  @Bean
  @Primary
  override def getNeo4jConfig: org.neo4j.ogm.config.Configuration = {
    val config = new org.neo4j.ogm.config.Configuration()
    config
      .driverConfiguration()
      .setDriverClassName(driverClass)
      .setEncryptionLevel("NONE")
    config
  }

}

object LocalGraphConfiguration extends LocalGraphConfiguration