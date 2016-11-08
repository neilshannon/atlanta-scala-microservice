import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ntsdev.config.LocalGraphConfiguration
import com.ntsdev.service.{AtlantaScalaMicroservice, TestDataService}
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext


class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with MockitoSugar with BeforeAndAfterAll {
  var microservice: AtlantaScalaMicroservice = _
  var testDataService: TestDataService = _

  override def testConfigSource = "akka.loglevel = WARNING"

  override def beforeAll() = {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[LocalGraphConfiguration])
    testDataService = context.getBean("testDataService").asInstanceOf[TestDataService]
    testDataService.loadTestData()
    
    microservice = context.getBean("atlantaScalaMicroservice").asInstanceOf[AtlantaScalaMicroservice]
    microservice.logger = NoLogging
  }

  "Service" should "return 200 for root context" in {
    Get() ~> microservice.routes ~> check {
      status shouldBe OK
    }
  }

  "Service" should "get a list of all persons" in {
    Get("/person") ~> microservice.routes ~> check {
      status shouldBe OK
      contentType shouldBe ContentTypes.`application/json`
      val responseString = responseAs[String]
      responseString shouldBe
        """[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[]}]},{"id":4,"firstName":"Maggie","lastName":"Simpson","google_id":"5","contacts":[{"id":0,"firstName":"Homer","lastName":"Simpson","google_id":"1","contacts":[]},{"id":1,"firstName":"Marge","lastName":"Simpson","google_id":"2","contacts":[]},{"id":2,"firstName":"Lisa","lastName":"Simpson","google_id":"3","contacts":[]},{"id":3,"firstName":"Bart","lastName":"Simpson","google_id":"4","contacts":[]}]}]""".stripMargin
    }
  }

}
