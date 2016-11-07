package com.ntsdev.run

import com.ntsdev.config.{LocalGraphConfiguration, RemoteGraphConfiguration, ServiceConfig}
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, Configuration}

@Configuration
class SpringRunner

object SpringRunner extends App with ServiceConfig {
  val logger = LoggerFactory.getLogger(getClass)

  if("cloud" equals config.getString("SPRING_PROFILES_ACTIVE")) {
    logger.info("Loading cloud configuration...")
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[RemoteGraphConfiguration])
  }
  else {
    import com.ntsdev.service.TestDataService
    logger.info("Loading local configuration...")
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[LocalGraphConfiguration])
    val testDataService: TestDataService = context.getBean("testDataService").asInstanceOf[TestDataService]
    testDataService.loadTestData()
  }
}

