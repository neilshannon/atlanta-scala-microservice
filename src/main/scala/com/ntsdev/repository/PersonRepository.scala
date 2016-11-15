package com.ntsdev.repository

import com.ntsdev.domain.Person
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.{Propagation, Transactional}

import java.lang.{Iterable => JIterable} //Spring Data Neo4J requires Java Collections

@Repository
@Transactional(propagation = Propagation.REQUIRED)
trait PersonRepository extends Neo4jRepository[Person]{

  @Query("MATCH (n:Person) WITH n MATCH p=(n)-[*0..1]->(m) RETURN DISTINCT p")
  def findConnections: JIterable[Person]

}
