package com.ntsdev.run

import com.ntsdev.config.{LocalGraphConfiguration, RemoteGraphConfiguration, EnvironmentConfig}
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, Configuration}

@Configuration
class SpringRunner

object SpringRunner extends App with EnvironmentConfig {
  val logger = LoggerFactory.getLogger(getClass)

  logger.info(s"Active Spring Profile: [$activeSpringProfile]")

  if("cloud" equals activeSpringProfile) {
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

