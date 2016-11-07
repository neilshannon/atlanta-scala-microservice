package com.ntsdev.service

import java.util.Collections

import org.neo4j.ogm.session.Session
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.{Propagation, Transactional}

import scala.collection.JavaConversions._
import scala.io.{Codec, Source}

@Service
@Profile(Array("test", "default"))
@Transactional(propagation = Propagation.REQUIRED)
class TestDataService {
  private val log = LoggerFactory.getLogger(getClass)
  private var loaded = false

  @Autowired
  val session: Session = null

  def loadTestData() = {
    session.purgeDatabase()
    log.info ("Loading test data...")

    val emptyMap = mapAsJavaMap[String, AnyRef](Collections.emptyMap[String, AnyRef]())
    session.query(loadDataFromFile("testdata.cql"), emptyMap)

    log.info("Test data loaded.")
    loaded = true
  }

  private def loadDataFromFile(fileName: String): String = {
    Source.fromURL(getClass.getResource("/" + fileName))(Codec.UTF8).mkString
  }

}
