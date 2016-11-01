package com.ntsdev.domain

import org.neo4j.ogm.annotation.{GraphId, NodeEntity}

@NodeEntity
case class Position(@GraphId var id: java.lang.Long = 0L, company: Company, isCurrent: Boolean, title: String)
