package com.ntsdev.http.routes

import akka.http.scaladsl.server.Directives
import com.ntsdev.domain.Person
import com.ntsdev.service.PersonService
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, native}

import scala.concurrent.ExecutionContext

class JsonRoutes(val personService: PersonService)(implicit executionContext: ExecutionContext) extends Directives with Json4sSupport {

  implicit val serialization = native.Serialization
  implicit val jsonFormats: Formats = DefaultFormats

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
