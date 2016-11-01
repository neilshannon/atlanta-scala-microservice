package com.ntsdev.run

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.ntsdev.ServiceConfig
import com.ntsdev.config.{LocalGraphConfiguration, RemoteGraphConfiguration}
import com.ntsdev.domain.PersonWithCompany
import com.ntsdev.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Component
import spray.json.DefaultJsonProtocol

import scala.annotation.meta.setter

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val personWithCompanyFormat = jsonFormat10(PersonWithCompany.apply)
}

@Component
class Service extends Protocols with Directives with ServiceConfig with App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher
  var logger = Logging(system, getClass)

  import scala.collection.JavaConversions._

  @(Autowired @setter)
  var personRepository: PersonRepository = _

  val routes = {
    logRequestResult("atlanta-scala-microservice") {
      pathSingleSlash {
        get {
          complete {
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Login Page</h1>")
          }
        }
      } ~
        pathPrefix("person"){
          get {
            complete(personRepository.findAll(1).map(_.toPersonWithCompany))
          }
        }
    }
  }
}

object AtlantaScalaMicroservice extends Service {

  private val interface: String = config.getString("http.interface")
  private val port: Int = config.getInt("http.port")

  logger.info("Starting http server...")

  Http().bindAndHandle(routes, interface, port)

  logger.info(s"Listening on port [$port] interface [$interface]")

  if("cloud" equals config.getString("SPRING_PROFILES_ACTIVE")) {
    SpringApplication.run(Array(RemoteGraphConfiguration.getClass.asInstanceOf[AnyRef]), args)
  }
  else {
    SpringApplication.run(Array(LocalGraphConfiguration.getClass.asInstanceOf[AnyRef]), args)
    LocalGraphConfiguration.loadTestData()
  }
}
