package com.ntsdev.service

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.ntsdev.config.ServiceConfig
import com.ntsdev.domain.PersonWithCompany
import org.springframework.stereotype.Service
import spray.json.DefaultJsonProtocol

import scala.collection.JavaConversions._
import scala.concurrent.Future

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val personWithCompanyFormat = jsonFormat10(PersonWithCompany.apply)
}

@Service
class AtlantaScalaMicroservice extends Protocols with Directives with ServiceConfig with GraphServices {
  private val interface: String = config.getString("http.interface")
  private val port: Int = config.getInt("http.port")

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor = system.dispatcher

  var logger = Logging(system, getClass)
  var binding: Future[Http.ServerBinding] = _

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
            testDataService.loadTestData()
            val nodes = personRepository.findAll(2)
            val people = nodes.map(_.toPersonWithCompany)
            complete(people)
          }
        }
    }
  }

  logger.info("Starting http server...")

  if(null == binding){
    binding = Http().bindAndHandle(routes, interface, port).flatMap(binding => {
      logger.info(s"Listening on port [$port] interface [$interface]")
      Future.successful(binding)
    })
  }
}


