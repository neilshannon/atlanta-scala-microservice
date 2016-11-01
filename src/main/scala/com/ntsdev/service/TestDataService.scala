package com.ntsdev.service

import com.ntsdev.domain.{Country, Location, Person}
import com.ntsdev.repository.PersonRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.{Propagation, Transactional}

import scala.annotation.meta.setter

@Service
@Transactional(propagation = Propagation.REQUIRED)
class TestDataService {
  private val log = LoggerFactory.getLogger(getClass)
  private var loaded = false

  @(Autowired @setter)
  var personRepository: PersonRepository = _

  def loadTestData(): Unit = {
    if(!loaded) {
      log.info("Loading test data...")

      val homer = new Person(
        1L,
        "Homer",
        "Simpson",
        "Safety Engineer",
        "1234",
        "Nuclear Power",
        None,
        Location(2L, "342 Evergreen Terrace", Country(3L, "US")),
        None,
        Set.empty[Person]
      )

      val marge = new Person(
        4L,
        "Marge",
        "Simpson",
        "Homemaker",
        "1236",
        "Homemaker",
        None,
        Location(2L, "342 Evergreen Terrace", Country(3L, "US")),
        None,
        Set(homer)
      )

      val lisa = new Person(
        5L,
        "Lisa",
        "Simpson",
        "Prodigy",
        "1235",
        "Musician",
        None,
        Location(2L, "342 Evergreen Terrace", Country(3L, "US")),
        None,
        Set(homer, marge)
      )

      val bart = new Person(
        6L,
        "Bart",
        "Simpson",
        "El Barto",
        "1236",
        "Troublemaker",
        None,
        Location(2L, "342 Evergreen Terrace", Country(3L, "US")),
        None,
        Set(homer, marge, lisa)
      )

      val maggie = new Person(
        7L,
        "Maggie",
        "Simpson",
        "Pacifier Lover",
        "1237",
        "Baby",
        None,
        Location(2L, "342 Evergreen Terrace", Country(3L, "US")),
        None,
        Set(homer, marge, bart, lisa)
      )

      List(homer, marge, bart, lisa, maggie).foreach(person => personRepository.save(person))

      log.info("Test data loaded.")
      loaded = true
    }

  }

}
