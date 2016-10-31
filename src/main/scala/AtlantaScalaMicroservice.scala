import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.ntsdev.ServiceConfig
import com.ntsdev.domain.PersonWithCompany
import com.typesafe.config.Config
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContextExecutor

trait Protocols extends DefaultJsonProtocol {
  implicit val personWithCompanyFormat = jsonFormat10(PersonWithCompany.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  val routes = {
    logRequestResult("atlanta-scala-microservice") {
      pathSingleSlash {
        get {
          complete {
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Login Page</h1>")
          }
        }
      }
    }
  }
}

object AtlantaScalaMicroservice extends App with Service with ServiceConfig {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val logger = Logging(system, getClass)

  private val interface: String = config.getString("http.interface")
  private val port: Int = config.getInt("http.port")

  logger.info("Starting http server...")

  Http().bindAndHandle(routes, interface, port)

  logger.info(s"Listening on port [$port] interface [$interface]")

}
