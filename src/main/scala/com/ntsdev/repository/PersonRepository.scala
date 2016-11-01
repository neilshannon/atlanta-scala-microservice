package com.ntsdev.repository

import com.ntsdev.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
trait PersonRepository extends GraphRepository[Person]{

}
