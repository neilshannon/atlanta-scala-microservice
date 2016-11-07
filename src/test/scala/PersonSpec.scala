import com.ntsdev.domain.Person
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConversions._

class PersonSpec extends FlatSpec with Matchers with MockitoSugar {

  "equals method" should "match on google_id" in {
    val emptyContacts = Set.empty[Person]
    val neil = Person(id = 1L, firstName = "Neil", lastName = "Shannon", google_id = "1234", contacts = emptyContacts)
    val anotherNeil = Person(id = 1L, firstName = "Neil", lastName = "Shannon", google_id = "1234", contacts = emptyContacts)

    neil should equal(anotherNeil)
  }
}
