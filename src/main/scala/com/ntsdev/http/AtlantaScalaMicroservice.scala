package com.ntsdev.http

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.ntsdev.config.EnvironmentConfig
import com.ntsdev.http.routes.{HtmlRoutes, JsonRoutes, TwitterRoutes}
import com.ntsdev.service.{PersonService, TwitterService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.concurrent.{ExecutionContext, Future}


@Service
class AtlantaScalaMicroservice extends Directives with EnvironmentConfig {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = system.dispatcher

  var _personService: PersonService = _

  private val logger = Logging(system, getClass)
  private val htmlRoutes = new HtmlRoutes()
  private val jsonRoutes = new JsonRoutes(_personService)
  private val twitterRoutes = new TwitterRoutes(new TwitterService)

  @Autowired
  def personService_=(service: PersonService){ //TODO: fix spring lifecycle ordering
    _personService = service
    jsonRoutes.personService = service
  }

  def routes = {
    htmlRoutes.route ~ jsonRoutes.route ~ twitterRoutes.route
  }

  logger.info("Starting http server...")

  Http().bindAndHandle(routes, interface, port).flatMap(binding => {
    logger.info(s"Listening on port [$port] interface [$interface]")
    Future.successful(binding)
  })


}


