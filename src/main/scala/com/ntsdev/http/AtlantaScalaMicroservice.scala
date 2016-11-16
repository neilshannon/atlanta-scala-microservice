package com.ntsdev.http

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.ntsdev.config.EnvironmentConfig
import com.ntsdev.http.routes.{HtmlRoutes, JsonRoutes}
import com.ntsdev.service.PersonService
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.concurrent.{ExecutionContext, Future}


@Service
class AtlantaScalaMicroservice extends Directives with Json4sSupport with EnvironmentConfig {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  @Autowired
  val personService: PersonService = null
  val logger = Logging(system, getClass)
  val htmlRoutes = new HtmlRoutes()

  def jsonRoutes = new JsonRoutes(personService)

  def routes = htmlRoutes.route ~ jsonRoutes.route

  logger.info("Starting http server...")

  Http().bindAndHandle(routes, interface, port).flatMap(binding => {
    logger.info(s"Listening on port [$port] interface [$interface]")
    Future.successful(binding)
  })

}


