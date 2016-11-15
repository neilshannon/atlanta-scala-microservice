package com.ntsdev.config

import org.neo4j.ogm.config.{Configuration => NeoConfig}
import org.springframework.context.annotation._
import org.springframework.data.neo4j.repository.config.EnableExperimentalNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Profile(Array("cloud"))
@Configuration
@EnableExperimentalNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@EnableTransactionManagement
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.domain.Person],
    classOf[com.ntsdev.service.PersonService],
    classOf[com.ntsdev.http.routes.JsonRoutes],
    classOf[com.ntsdev.http.AtlantaScalaMicroservice]
  )
)
@Primary
class RemoteGraphConfiguration extends GraphConfiguration with EnvironmentConfig {

  private final val driverClass = "org.neo4j.ogm.drivers.bolt.driver.BoltDriver"

  @Bean
  @Primary
  override def getNeo4jConfig: org.neo4j.ogm.config.Configuration = {
    val URI = s"bolt://$neo4jUser:$neo4jPass@$neo4jHost:$neo4jPort"
    buildGraphConfig(URI)
  }


  private def buildGraphConfig(URI: String): NeoConfig = {
    val graphConfig: NeoConfig = new NeoConfig()
    graphConfig
      .driverConfiguration()
      .setDriverClassName(driverClass)
      .setURI(URI)
      .setEncryptionLevel("NONE")
      .setConnectionPoolSize(150)

    graphConfig
  }

}

object RemoteGraphConfiguration extends RemoteGraphConfiguration



