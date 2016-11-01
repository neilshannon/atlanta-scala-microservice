package com.ntsdev.service

import com.ntsdev.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.annotation.meta.setter

@Service
@Transactional
trait GraphServices {
  @(Autowired @setter)
  var personRepository: PersonRepository = _

  @(Autowired @setter)
  var testDataService: TestDataService = _
}
