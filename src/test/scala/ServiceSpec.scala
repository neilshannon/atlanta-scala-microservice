import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ntsdev.service.AtlantaScalaMicroservice
import org.scalatest._
import org.scalatest.mock.MockitoSugar


class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with MockitoSugar {
  val service = new AtlantaScalaMicroservice

  override def testConfigSource = "akka.loglevel = WARNING"
  service.config = testConfig
  service.logger = NoLogging

  "Service" should "display the login page" in {
    Get("/") ~> service.routes ~> check {
      status shouldBe OK
      contentType shouldBe ContentTypes.`text/html(UTF-8)`
      responseAs[String] shouldBe "<h1>Login Page</h1>"
    }
  }

}
