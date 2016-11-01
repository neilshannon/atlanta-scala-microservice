package com.ntsdev.config

import com.ntsdev.neo4j.Neo4jConnectivity
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation._
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(basePackages = Array("com.ntsdev.run"))
@EnableAutoConfiguration
@Profile(Array("cloud"))
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
class RemoteGraphConfiguration extends Neo4jConfiguration with Neo4jConnectivity {
    private val log = LoggerFactory.getLogger(getClass)

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

        log.info(s"Bootstrapping remote graph configuration at [$neo4jHost:$neo4jPort]")

        config
    }

    @Override
    @Bean
    @Primary
    def getSessionFactory: SessionFactory = {
        new SessionFactory(getNeo4jConfig, "com.ntsdev.domain")
    }

}
object RemoteGraphConfiguration extends RemoteGraphConfiguration



