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
@Profile(Array("cloud"))
@EnableNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
@EnableTransactionManagement
class RemoteGraphConfiguration extends Neo4jConfiguration with Neo4jConnectivity {
    private val log = LoggerFactory.getLogger(getClass)

    @Bean
    def getNeo4jConfig: Configuration = {
        val URI = s"bolt://$neo4jUser:$neo4jPass@$neo4jHost:$neo4jPort"

        val config: Configuration = new Configuration()
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
    def getSessionFactory: SessionFactory = {
        new SessionFactory(getNeo4jConfig, "com.ntsdev.domain")
    }
}
object RemoteGraphConfiguration extends RemoteGraphConfiguration



