import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ntsdev.config.LocalGraphConfiguration
import com.ntsdev.http.AtlantaScalaMicroservice
import com.ntsdev.service.TestDataService
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext


class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with MockitoSugar with BeforeAndAfterAll {
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
    Get() ~> httpService.routes ~> check {
      status shouldBe Found
      val locationHeader = response.headers.find(_.name() == "Location")
      locationHeader.foreach(_.value() shouldBe "/site/index.html")
    }
  }

  "Service" should "get a list of all persons" in {
    Get("/person") ~> httpService.routes ~> check {
      status shouldBe OK
      contentType shouldBe ContentTypes.`application/json`
      val responseString = responseAs[String]
      responseString shouldBe
        """[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]}]}]""".stripMargin
    }
  }

}
