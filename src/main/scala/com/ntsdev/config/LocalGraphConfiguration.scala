package com.ntsdev.config

import com.ntsdev.domain.{Country, Location, Person}
import com.ntsdev.neo4j.Neo4jConnectivity
import com.ntsdev.repository.PersonRepository
import org.neo4j.ogm.session.{Session, SessionFactory}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation._
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.transaction.annotation.{EnableTransactionManagement, Transactional}

import scala.annotation.meta.setter

@Profile(Array("default"))
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = Array("com.ntsdev.repository"))
class LocalGraphConfiguration extends Neo4jConfiguration with Neo4jConnectivity {
  private val log = LoggerFactory.getLogger(getClass)

  @(Autowired @setter)
  var personRepository: PersonRepository = _

  @Bean
  @Primary
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  def getLocalConfiguration: org.neo4j.ogm.config.Configuration = {
    val config = new org.neo4j.ogm.config.Configuration()
    config
      .driverConfiguration()
      .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
      .setURI("file:///var/tmp/graph.db")

    log.info("Bootstrapping local graph configuration")

    config
  }

  @Override
  @Bean
  @Primary
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  override def getSessionFactory: SessionFactory = {
    new SessionFactory(getLocalConfiguration, "com.ntsdev.domain")
  }

  @Bean
  @Primary
  @Override
  @Scope(proxyMode = ScopedProxyMode.INTERFACES)
  override def getSession: Session = {
    getSessionFactory.openSession()
  }

}

object LocalGraphConfiguration extends LocalGraphConfiguration {

  @Transactional
  def loadTestData(): Unit = {
    val homer = new Person(
      0L,
      "Homer",
      "Simpson",
      "Safety Engineer",
      "1234",
      "Nuclear Power",
      None,
      Location(0L, "342 Evergreen Terrace", Country("US")),
      None,
      Set.empty[Person]
    )

    val marge = new Person(
      0L,
      "Marge",
      "Simpson",
      "Homemaker",
      "1236",
      "Homemaker",
      None,
      Location(0L, "342 Evergreen Terrace", Country("US")),
      None,
      Set(homer)
    )

    val lisa = new Person(
      0L,
      "Lisa",
      "Simpson",
      "Prodigy",
      "1235",
      "Homemaker",
      None,
      Location(0L, "342 Evergreen Terrace", Country("US")),
      None,
      Set(homer, marge)
    )

    val bart = new Person(
      0L,
      "Bart",
      "Simpson",
      "El Barto",
      "1236",
      "Troublemaker",
      None,
      Location(0L, "342 Evergreen Terrace", Country("US")),
      None,
      Set(homer, marge, lisa)
    )

    val maggie = new Person(
      0L,
      "Maggie",
      "Simpson",
      "Pacifier Lover",
      "1237",
      "Baby",
      None,
      Location(0L, "342 Evergreen Terrace", Country("US")),
      None,
      Set(homer, marge, bart, lisa)
    )

    List(homer, marge, bart, lisa, maggie).foreach(person => personRepository.save(person))
  }
}