package com.ntsdev.config

import com.ntsdev.service.TestDataService
import org.neo4j.ogm.session.SessionFactory
import org.springframework.context.annotation.Bean
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager

trait GraphConfiguration {

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

  def getNeo4jConfig: org.neo4j.ogm.config.Configuration

}
