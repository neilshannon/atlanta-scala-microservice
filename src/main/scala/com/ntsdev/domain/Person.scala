package com.ntsdev.domain

import org.neo4j.ogm.annotation.{GraphId, NodeEntity, Relationship}

import scala.annotation.meta.setter

@NodeEntity
case class Person(
                   @(GraphId @setter) var id: java.lang.Long = 0L,
                   firstName: String,
                   lastName: String,
                   headline: String,
                   linkedin_id: String,
                   industry: String,
                   pictureUrl: Option[String],
                   location: Location,
                   positions: Option[Array[Position]] = None,
                   @Relationship(`type`="CONNECTED_TO") connections: Set[Person]
                 ) {

  def toPersonWithCompany: PersonWithCompany = {
    val position = currentPosition
    PersonWithCompany(
      firstName,
      lastName,
      headline,
      linkedin_id,
      industry,
      pictureUrl.getOrElse(""),
      location.name,
      position.map(_.title).getOrElse(""),
      position.map(_.company.name).getOrElse(""),
      position.map(_.company.linkedin_id).getOrElse("")
    )
  }

  def currentPosition = {
    positions.map { actualPositions => actualPositions.filter(_.isCurrent).head }
  }
}