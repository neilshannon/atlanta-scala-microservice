package com.ntsdev.service

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.ntsdev.domain.Person
import com.ntsdev.repository.PersonRepository
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, native}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.annotation.meta.setter
import scala.collection.Iterable
import scala.collection.JavaConversions._
import scala.concurrent.Future


@Service
class AtlantaScalaMicroservice extends Directives with Json4sSupport {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher

  implicit val jsonFormats: Formats = DefaultFormats
  implicit val serialization = native.Serialization

  var logger = Logging(system, getClass)
  var binding: Future[Http.ServerBinding] = _

  @(Autowired @setter)
  var personRepository: PersonRepository = _

  val config = ConfigFactory.systemEnvironment().withFallback(ConfigFactory.load())
  private val interface: String = config.getString("http.interface")
  private val port: Int = config.getInt("http.port")

  val jsonRoutes = {
    logRequestResult("atlanta-scala-microservice") {
      get {
        pathPrefix("person"){
          val people: Iterable[Person] = personRepository.findConnections()
          val shallowPeople = peopleWithContactsDepthOne(people)
          complete(shallowPeople)
        }
      }
    }
  }

  val htmlRoutes = {
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"<html><body>Login Page</body></html>"))
      }
    }
  }

  val routes = jsonRoutes ~ htmlRoutes

  logger.info("Starting http server...")

  if(null == binding){
    binding = Http().bindAndHandle(routes, interface, port).flatMap(binding => {
      logger.info(s"Listening on port [$port] interface [$interface]")
      Future.successful(binding)
    })
  }


  /**
    * To avoid cycles in our graph and stack overflow errors when serializing, we only go one level deep
    * @param people an Iterable of Persons with potential circular references via the contacts parameter
    * @return an Iterable of People with contacts of depth 1
    */
  private def peopleWithContactsDepthOne(people: Iterable[Person]): Iterable[Person] = {
    people.map(rootPerson => {
      val personContacts: Set[Person] = rootPerson.contacts.toSet
      val subContacts: Set[Person] = personContacts.map(contact => contact.copy(contacts = Set.empty[Person]))
      rootPerson.copy(contacts = subContacts)
    })
  }
}


