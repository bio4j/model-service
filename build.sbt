Nice.scalaProject

organization := "bio4j"

name := "model-service"

description := "model-service project"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "ohnosequences" % "typed-graphs" % "0.1.0-SNAPSHOT",
  "bio4j" % "bio4j" % "0.12.0-SNAPSHOT",
  "net.databinder" %% "unfiltered-filter" % "0.8.0",
  "net.databinder" %% "unfiltered-jetty" % "0.8.0",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)

dependencyOverrides ++= Set(
  "commons-codec" % "commons-codec" % "1.7"
)
