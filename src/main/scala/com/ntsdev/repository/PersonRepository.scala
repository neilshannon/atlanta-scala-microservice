package com.ntsdev.repository

import com.ntsdev.domain.Person
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
trait PersonRepository extends Neo4jRepository[Person]{

}
