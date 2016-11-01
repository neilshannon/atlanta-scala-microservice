package com.ntsdev.domain

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