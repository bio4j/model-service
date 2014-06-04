Nice.scalaProject

organization := "bio4j"

name := "model-service"

description := "model-service project"

bucketSuffix := "era7.com"

skip in update := true

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "bio4j" %% "scala-model" % "0.1.0-SNAPSHOT",
  "ohnosequences" %% "type-sets" % "0.4.0-SNAPSHOT",
  "net.databinder" %% "unfiltered-filter" % "0.8.0",
  "net.databinder" %% "unfiltered-jetty" % "0.8.0",
  "io.argonaut" %% "argonaut" % "6.0.4",
  "io.argonaut" %% "argonaut-unfiltered" % "6.0.4",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)

dependencyOverrides ++= Set(
  "commons-codec" % "commons-codec" % "1.7",
  "net.databinder" %% "unfiltered" % "0.8.0",
  "tomcat" % "jasper-compiler" % "5.5.23",
  "tomcat" % "jasper-runtime" % "5.5.23"
)

Revolver.settings
