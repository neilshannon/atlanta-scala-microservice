import java.util.Collections

import com.ntsdev.domain.Person
import com.ntsdev.serialization.PersonSerializer
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConversions._

class PersonSerializerSpec extends FlatSpec with Matchers {

  "the Person serializer" should "serialize a person with contacts" in {
    val emptyContacts = Collections.emptySet[Person]

    val john = Person(id = 1L, firstName = "John", lastName = "Verrone", google_id = "1234", contacts = emptyContacts)
    val margo = Person(id = 2L, firstName = "Margo", lastName = "Roy", google_id = "1235", contacts = emptyContacts)
    val anushka = Person(id = 3L, firstName = "Anushka", lastName = "Sharma", google_id = "1236", contacts = emptyContacts)
    val chad = Person(id = 4L, firstName = "Chad", lastName = "Davis", google_id = "1237", contacts = emptyContacts)

    val someContacts = Set(john, margo, anushka, chad)

    val yuriy = Person(id = 5L, firstName = "Yuriy", lastName = "Kozubskyi", google_id = "1238", contacts = someContacts)
    val sheen = Person(id = 6L, firstName = "Sheen", lastName = "Jacob", google_id = "1239", contacts = emptyContacts)
    val sydney = Person(id = 7L, firstName = "Sydney", lastName = "Smith", google_id = "1240", contacts = someContacts)
    val omie = Person(id = 8L, firstName = "Omie", lastName = "Walls", google_id = "1241", contacts = emptyContacts)

    val teamContacts = Set(john, margo, chad, anushka, yuriy, sheen, sydney, omie)

    val neil = Person(id = 0L, firstName = "Neil", lastName = "Shannon", google_id = "1234", contacts = teamContacts)

    implicit val formats = DefaultFormats + PersonSerializer

    val json = write(neil)

    json shouldEqual(
      """{"id":0,"firstName":"Neil","lastName":"Shannon","google_id":"1234","contacts":[{"id":5,"firstName":"Yuriy","lastName":"Kozubskyi","google_id":"1238","contacts":[{"id":1,"firstName":"John","lastName":"Verrone","google_id":"1234","contacts":[]},{"id":2,"firstName":"Margo","lastName":"Roy","google_id":"1235","contacts":[]},{"id":3,"firstName":"Anushka","lastName":"Sharma","google_id":"1236","contacts":[]},{"id":4,"firstName":"Chad","lastName":"Davis","google_id":"1237","contacts":[]}]},{"id":1,"firstName":"John","lastName":"Verrone","google_id":"1234","contacts":[]},{"id":6,"firstName":"Sheen","lastName":"Jacob","google_id":"1239","contacts":[]},{"id":2,"firstName":"Margo","lastName":"Roy","google_id":"1235","contacts":[]},{"id":7,"firstName":"Sydney","lastName":"Smith","google_id":"1240","contacts":[{"id":1,"firstName":"John","lastName":"Verrone","google_id":"1234","contacts":[]},{"id":2,"firstName":"Margo","lastName":"Roy","google_id":"1235","contacts":[]},{"id":3,"firstName":"Anushka","lastName":"Sharma","google_id":"1236","contacts":[]},{"id":4,"firstName":"Chad","lastName":"Davis","google_id":"1237","contacts":[]}]},{"id":3,"firstName":"Anushka","lastName":"Sharma","google_id":"1236","contacts":[]},{"id":8,"firstName":"Omie","lastName":"Walls","google_id":"1241","contacts":[]},{"id":4,"firstName":"Chad","lastName":"Davis","google_id":"1237","contacts":[]}]}""".stripMargin
      )

  }


}
