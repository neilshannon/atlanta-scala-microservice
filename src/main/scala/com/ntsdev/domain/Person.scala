package com.ntsdev.domain

import org.neo4j.ogm.annotation.{GraphId, NodeEntity, Relationship}
import org.springframework.data.annotation.PersistenceConstructor

import scala.annotation.meta.setter
import scala.collection.JavaConversions._

@NodeEntity
case class Person @PersistenceConstructor()(
                   @(GraphId @setter) var id: java.lang.Long,
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