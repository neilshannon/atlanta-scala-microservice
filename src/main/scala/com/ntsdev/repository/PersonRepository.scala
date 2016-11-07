package com.ntsdev.repository

import com.ntsdev.domain.Person
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.{Propagation, Transactional}

@Repository
@Transactional(propagation = Propagation.REQUIRED)
trait PersonRepository extends Neo4jRepository[Person]{
  @Query("MATCH (n:Person) WITH n MATCH p=(n)-[*0..1]->(m) RETURN DISTINCT p")
  def findConnections(): java.lang.Iterable[Person]
}
