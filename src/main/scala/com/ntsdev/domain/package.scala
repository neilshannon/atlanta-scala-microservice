package com.ntsdev

import scala.annotation.meta.field

package object domain {
  type GraphId = org.neo4j.ogm.annotation.GraphId @field
  type Id = org.springframework.data.annotation.Id @field
}
