import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ntsdev.config.LocalGraphConfiguration
import com.ntsdev.domain.Person
import com.ntsdev.http.AtlantaScalaMicroservice
import com.ntsdev.http.routes.TwitterRoutes
import com.ntsdev.service.{TestDataService, TwitterService}
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.concurrent.Future
import scala.io.Source


class AtlantaScalaMicroserviceSpec extends FlatSpec with Matchers with ScalatestRouteTest with MockitoSugar with BeforeAndAfterAll {
  var httpService: AtlantaScalaMicroservice = _
  var testDataService: TestDataService = _

  override def testConfigSource = "akka.loglevel = WARNING"

  override def beforeAll() = {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[LocalGraphConfiguration])
    testDataService = context.getBean("testDataService").asInstanceOf[TestDataService]
    testDataService.loadTestData()

    httpService = context.getBean("atlantaScalaMicroservice").asInstanceOf[AtlantaScalaMicroservice]
  }

  "Service" should "return redirect to index.html at root context" in {
    Get.apply() ~> httpService.routes ~> check {
      status shouldBe Found
      val locationHeader = response.headers.find(_.name() == "Location")
      locationHeader.foreach(_.value() shouldBe "/site/index.html")
    }
  }

  "Service" should "return a js file when requested" in {
    Get.apply("/site/js/lodash.min.js") ~> httpService.routes ~> check {
      status shouldBe OK
    }
  }

  "Service" should "get a list of all persons" in {
    val contactsJson = loadFile("contacts.json")

    Get.apply("/person") ~> httpService.routes ~> check {
      status shouldBe OK
      contentType shouldBe ContentTypes.`application/json`
      val responseJson = responseAs[String]
      responseJson shouldEqual contactsJson
    }
  }

  private def loadFile(fileName: String): String = {
    Source.fromInputStream(getClass.getResourceAsStream(fileName)).mkString
  }
}
