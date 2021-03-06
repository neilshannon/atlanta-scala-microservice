enablePlugins(JavaAppPackaging)

enablePlugins(UniversalPlugin)

name := "atlanta-scala-microservice"
organization := "com.ntsdev"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Maven Central" at "http://central.maven.org/maven2/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "JCenter" at "https://jcenter.bintray.com/",
  "Spring Maven" at "http://repo.spring.io/libs-release",
  "Spring Snapshots" at "http://repo.spring.io/libs-snapshot",
  "Neo4j Maven Snapshots" at "http://m2.neo4j.org/content/repositories/snapshots",
  "Artifactory" at "https://maven.artifactory.homedepot.com/artifactory/libs-release-local/"
)

publishTo := Some("Artifactory Realm" at "https://maven.artifactory.homedepot.com/artifactory/libs-release-local")
credentials += Credentials("Artifactory Realm", "maven.artifactory.homedepot.com", sys.env.getOrElse("artifactory_user", ""), sys.env.getOrElse("artifactory_password", ""))

libraryDependencies ++= {
  val akkaV       = "2.4.11"
  val scalaTestV  = "2.2.6"
  val springV     = "4.3.1.RELEASE"
  val springDataV = "4.2.0.M1"
  val neo4jOgmV   = "2.0.5"

  Seq(
    "com.typesafe.akka"                  %% "akka-http-core" % akkaV,
    "com.typesafe.akka"                  %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka"                  %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka"                  %% "akka-http-testkit" % akkaV,
    "com.softwaremill.akka-http-session" %% "core" % "0.2.7",
    "org.springframework"                 % "spring-core" % springV,
    "org.springframework"                 % "spring-aop" % springV,
    "org.springframework"                 % "spring-aspects" % springV,
    "org.springframework"                 % "spring-beans" % springV,
    "org.springframework"                 % "spring-context" % springV,
    "org.springframework.data"            % "spring-data-commons" % "1.13.0.M1",
    "org.springframework.data"            % "spring-data-neo4j" % springDataV,
    "org.neo4j"                           % "neo4j-ogm-api" % neo4jOgmV,
    "org.neo4j"                           % "neo4j-ogm-core" % neo4jOgmV,
    "org.neo4j"                           % "neo4j-ogm-compiler" % neo4jOgmV,
    "org.neo4j"                           % "neo4j-ogm-bolt-driver" % neo4jOgmV,
    "org.neo4j"                           % "neo4j-ogm-embedded-driver" % neo4jOgmV,
    "org.neo4j"                           % "neo4j" % "3.0.6",
    "com.typesafe"                        % "config" % "1.3.0",
    "ch.qos.logback"                      % "logback-classic" % "1.1.3",
    "com.danielasfregola"                %% "twitter4s" % "2.0",
    "com.github.scribejava"               % "scribejava-apis" % "3.3.0",
    "org.scalatest"                      %% "scalatest" % scalaTestV % "test",
    "org.mockito"                         % "mockito-core" % "2.2.9" % "test",
    "org.springframework"                 % "spring-test" % springV % "test"

  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

mainClass in Global := Some("com.ntsdev.run.SpringRunner")

Revolver.settings
