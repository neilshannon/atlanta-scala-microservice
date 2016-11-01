package com.ntsdev.domain

import org.neo4j.ogm.annotation.{GraphId, NodeEntity, Property}


@NodeEntity
case class Location(@GraphId var id: java.lang.Long = 0L, @Property name: String, @Property country: Country){
  override def toString = {
    name
  }
}

case class Country(@Property code: String)