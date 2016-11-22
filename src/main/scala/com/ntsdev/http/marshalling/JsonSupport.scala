package com.ntsdev.http.marshalling

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.ntsdev.domain.Person
import spray.json.{JsonFormat, _}

import scala.collection.JavaConversions._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object JLongFormat extends JsonFormat[java.lang.Long]{
    def write(obj: java.lang.Long) = JsNumber(obj)
    def read(json: JsValue) : java.lang.Long = json match {
      case JsNumber(value: BigDecimal) => value.longValue()
      case x => deserializationError("Expected Long as BigDecimal, but got " + x)
    }
  }

  implicit object jSetPersonFormat extends JsonFormat[java.util.Set[Person]] {
    def write(set: java.util.Set[Person]) = JsArray(set.toSet.map((value: Person) => value.toJson).toVector)
    def read(json: JsValue) = json match {
      case JsArray(elements) => elements.map(_.convertTo[Person]).toSet[Person]
      case x => deserializationError("Expected Set as JsArray, but got " + x)
    }
  }

  implicit val personFormat: JsonFormat[Person] = lazyFormat(
    jsonFormat(Person, "id", "firstName", "lastName", "twitter_id", "contacts")
  )
}
