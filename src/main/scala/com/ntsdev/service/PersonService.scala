package com.ntsdev.service

import com.ntsdev.domain.Person
import com.ntsdev.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.Iterable
import scala.collection.JavaConversions._

@Service
class PersonService {

  @Autowired
  val personRepository: PersonRepository = null

  /**
    * To avoid cycles in our graph and stack overflow errors when serializing, we only go one level deep
    * @return an Iterable of People with contacts of depth 1
    */
  def peopleWithContactsDepthOne: Iterable[Person] = {
    val people: Iterable[Person] = personRepository.findConnections

    people.map(rootPerson => {
      val personContacts: Set[Person] = rootPerson.contacts.toSet
      val subContacts: Set[Person] = personContacts.map(contact => contact.copy(contacts = Set.empty[Person]))
      rootPerson.copy(contacts = subContacts)
    })
  }
}
