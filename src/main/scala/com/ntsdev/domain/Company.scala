package com.ntsdev.domain

import org.neo4j.ogm.annotation.{GraphId, NodeEntity}

@NodeEntity
case class Company(
                    @GraphId var id: java.lang.Long,
                    linkedin_id: String,
                    industry: String,
                    name: String,
                    size: Option[String],
                    `type`: Option[String]
                  )