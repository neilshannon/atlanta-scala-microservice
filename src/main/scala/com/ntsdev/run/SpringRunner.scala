package com.ntsdev.run

import com.ntsdev.config.{LocalGraphConfiguration, RemoteGraphConfiguration, ServiceConfig}
import com.ntsdev.service.TestDataService
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, ComponentScan, Configuration}

@Configuration
@ComponentScan(
  basePackageClasses = Array(
    classOf[com.ntsdev.repository.PersonRepository],
    classOf[com.ntsdev.service.TestDataService],
    classOf[com.ntsdev.service.AtlantaScalaMicroservice])
)
class SpringRunner

object SpringRunner extends App with ServiceConfig {
  if("cloud" equals config.getString("SPRING_PROFILES_ACTIVE")) {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[RemoteGraphConfiguration])
  }
  else {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[LocalGraphConfiguration])
    val testDataService: TestDataService = context.getBean("TestDataService").asInstanceOf[TestDataService]
    testDataService.loadTestData()
  }
}

