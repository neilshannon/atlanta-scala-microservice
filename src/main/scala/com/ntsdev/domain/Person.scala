package com.ntsdev.domain

import com.ntsdev.domain.Person.GraphId
import org.neo4j.ogm.annotation.{NodeEntity, Relationship}

import scala.annotation.meta.field
import scala.collection.JavaConversions._

@NodeEntity
case class Person (
                   @GraphId var id: java.lang.Long,
                   var firstName: String,
                   var lastName: String,
                   var google_id: String,
                   @Relationship(`type`="CONNECTED") var contacts: java.util.Set[Person]
                 ){

  def this() = this(null, null, null, null, Set.empty[Person]) //this is for Spring Data Neo4j

  override def toString: String = {
    s"id: [$id], " +
    s"firstName: [$firstName], " +
    s"lastName: [$lastName], " +
    s"connections: [${contacts.map(_.firstName).mkString(",")}]"
  }

  @Override
  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case x: Person =>
        x.id == this.id
      case _ =>
        false
    }
  }

  @Override
  override def hashCode(): Int = {
    if(id != null) id.hashCode() else 0
  }
}

object Person extends Person {
  type GraphId = org.neo4j.ogm.annotation.GraphId @field
  type Id = org.springframework.data.annotation.Id @field
}