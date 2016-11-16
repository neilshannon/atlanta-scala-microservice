import com.ntsdev.domain.Person
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConversions._

class PersonSpec extends FlatSpec with Matchers with MockitoSugar {

  val emptyContacts = Set.empty[Person]
  val neil = Person(id = 1234L, firstName = "Neil", lastName = "Shannon", google_id = "1234", contacts = emptyContacts)


  "equals method" should "match on google_id" in {
    val anotherNeil = Person(id = 1234L, firstName = "Neil", lastName = "Shannon", google_id = "1234", contacts = emptyContacts)
    neil should equal(anotherNeil)
  }

  "hashCode method" should "be zero if id is null" in {
    val aPerson = new Person()
    aPerson.hashCode shouldEqual 0
  }

  "hashCode method" should "equal the hashCode of the id" in {
    neil.hashCode shouldEqual 1234L.hashCode
  }

  "toString method" should "equal the toString format" in {
    neil.toString shouldEqual "id: [1234], firstName: [Neil], lastName: [Shannon], connections: []"
  }



}
