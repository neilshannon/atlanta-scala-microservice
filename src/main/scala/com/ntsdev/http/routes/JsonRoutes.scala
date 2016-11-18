package com.ntsdev.http.routes

import akka.http.scaladsl.server.Directives
import com.danielasfregola.twitter4s.http.unmarshalling.{JsonSupport => Twitter4sJsonSupport}
import com.ntsdev.domain.Person
import com.ntsdev.http.marshalling.JsonSupport
import com.ntsdev.service.PersonService

import scala.concurrent.ExecutionContext

class JsonRoutes(var personService: PersonService)(implicit executionContext: ExecutionContext) extends Directives with Twitter4sJsonSupport with JsonSupport {

  val route = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("person"){
          val shallowPeople: Iterable[Person] = personService.peopleWithContactsDepthOne
          complete(shallowPeople)
        }
      }
    }
  }
}
