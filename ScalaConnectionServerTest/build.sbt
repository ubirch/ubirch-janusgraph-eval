name := "ScalaConnectionServerTest"

version := "0.1"

scalaVersion := "2.12.8"


libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.json4s"   %% "json4s-native" % "3.5.2",
  "org.json4s" %% "json4s-jackson" % "3.5.2",
  "org.scalatra" %% "scalatra-swagger"  % "2.6.5",
  "com.michaelpollmeier" %% "gremlin-scala" % "3.3.5.2",
  "org.janusgraph" % "janusgraph-core" % "0.3.1",
  "org.janusgraph" % "janusgraph-hbase" % "0.3.1",
  "org.janusgraph" % "janusgraph-cql" % "0.3.1",
  "org.janusgraph" % "janusgraph-es" % "0.3.1",
  "org.apache.tinkerpop" % "gremlin-driver" % "3.3.3",
  "net.liftweb" %% "lift-json" % "3.3.0", // for JSON parsing
  // https://mvnrepository.com/artifact/com.tinkerpop.gremlin/gremlin-java
  "com.tinkerpop.gremlin" % "gremlin-java" % "2.6.0"
)