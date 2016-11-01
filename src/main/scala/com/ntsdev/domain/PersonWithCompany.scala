package com.ntsdev.domain

import org.neo4j.ogm.annotation.Transient

@Transient
final case class PersonWithCompany(
                      firstName: String,
                      lastName: String,
                      headline: String,
                      linkedin_id: String,
                      industry: String,
                      pictureUrl: String,
                      location: String,
                      title: String,
                      company: String,
                      company_id: String
                    )