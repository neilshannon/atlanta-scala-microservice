package com.ntsdev.serialization

import com.ntsdev.domain.Person
import org.json4s._
import scala.collection.JavaConversions._

case object PersonSerializer extends CustomSerializer[Person](format => (
  {
    case JObject(
      JField("id", JLong(id)) ::
      JField("firstName", JString(firstName)) ::
      JField("lastName", JString(lastName)) ::
      JField("google_id", JString(google_id)) ::
      JField("contacts", JArray(contacts)) :: _
    ) => Person(id, firstName, lastName, google_id, contacts = Set.empty[Person]) //TODO: fix
  },
  {
    case x: Person => Extraction.decompose(x)(DefaultFormats)
  }
  )
)


