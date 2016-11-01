enablePlugins(JavaAppPackaging)

name := "atlanta-scala-microservice"
organization := "com.ntsdev"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Maven Central" at "http://central.maven.org/maven2/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "JCenter" at "https://jcenter.bintray.com/",
  "Spring Maven" at "http://repo.spring.io/libs-release"
)

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaV       = "2.4.7"
  val scalaTestV  = "2.2.6"
  val springBootV = "1.4.1.RELEASE"

  Seq(
    "com.typesafe.akka"       %% "akka-http-core" % akkaV,
    "com.typesafe.akka"       %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka"       %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka"       %% "akka-http-testkit" % akkaV,
    "org.springframework.boot" % "spring-boot-starter-data-neo4j" % springBootV,
    "org.neo4j"                % "neo4j-ogm-bolt-driver" % "2.0.5",
    "org.neo4j"                % "neo4j-ogm-embedded-driver" % "2.0.5",
    "com.typesafe"             % "config" % "1.3.0",
    "org.json4s"              %% "json4s-native" % "3.3.0",
    "ch.qos.logback"           % "logback-classic" % "1.1.3",
    "org.scalatest"           %% "scalatest" % scalaTestV % "test"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

mainClass in Global := Some("com.ntsdev.com.ntsdev.run.AtlantaScalaMicroservice")

Revolver.settings
