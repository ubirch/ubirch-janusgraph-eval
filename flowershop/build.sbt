val ScalatraVersion = "2.6.5"

organization := "ubirch"

name := "swagger-scalatra-REST-API"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases
resolvers += Resolver.sonatypeRepo("releases")  // Or "snapshots"

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=58762"
)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.15.v20190215" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % "2.6.5",
  "org.json4s"   %% "json4s-native" % "3.5.2",
"org.json4s" %% "json4s-jackson" % "3.5.2",
"org.scalatra" %% "scalatra-swagger"  % "2.6.5",
  "com.michaelpollmeier" %% "gremlin-scala" % "3.4.1.1",
  "org.janusgraph" % "janusgraph-core" % "0.3.1",
  "org.janusgraph" % "janusgraph-cql" % "0.3.1",
  "org.janusgraph" % "janusgraph-es" % "0.3.1",
  "org.apache.tinkerpop" % "gremlin-driver" % "3.3.3",
  "net.liftweb" %% "lift-json" % "3.3.0", // for JSON parsing
  // https://mvnrepository.com/artifact/com.tinkerpop.gremlin/gremlin-java
"com.tinkerpop.gremlin" % "gremlin-java" % "2.6.0"

)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
