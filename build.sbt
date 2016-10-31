enablePlugins(JavaAppPackaging)

name := "atlanta-scala-microservice"
organization := "com.ntsdev"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "anormcypher" at "http://repo.anormcypher.org/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  val akkaV       = "2.4.7"
  val scalaTestV  = "2.2.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
    "com.typesafe"       % "config" % "1.3.0",
    "org.json4s"        %% "json4s-native" % "3.3.0",
    "ch.qos.logback"     % "logback-classic" % "1.1.3" % "runtime",
    "org.anormcypher"   %% "anormcypher" % "0.9.1",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

Revolver.settings
