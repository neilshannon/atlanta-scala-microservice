package com.ntsdev.run

import com.ntsdev.config.{LocalGraphConfiguration, RemoteGraphConfiguration, ServiceConfig}
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, ComponentScan, Configuration}

@Configuration
@ComponentScan(basePackages = Array("com.ntsdev.*"))
class SpringRunner

object SpringRunner extends App with ServiceConfig {
  if("cloud" equals config.getString("SPRING_PROFILES_ACTIVE")) {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[RemoteGraphConfiguration])
  }
  else {
    val context: ApplicationContext = new AnnotationConfigApplicationContext(classOf[LocalGraphConfiguration])
  }
}

