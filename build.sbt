name := "twitch-web-push"

version := "0.1"

scalaVersion := "2.12.2"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)

// com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
enablePlugins(JavaServerAppPackaging)
