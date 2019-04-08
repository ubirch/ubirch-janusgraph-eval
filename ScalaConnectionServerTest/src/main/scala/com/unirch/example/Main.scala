package com.unirch.example

import org.apache.tinkerpop.gremlin.structure.{Edge, Vertex}
import org.janusgraph.core.JanusGraphFactory

import gremlin.scala._
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerIoRegistryV3d0



object Main{
  def main(args: Array[String]): Unit = {
    val serializer = new GryoMessageSerializerV3d0(GryoMapper.build.addRegistry(TinkerIoRegistryV3d0.instance))
    val cluster = Cluster.build.addContactPoint("localhost").port(8182).serializer(serializer).create
    implicit val graph = EmptyGraph.instance.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))
/*    graph.traversal.V.drop.iterate
    val Name = Key[String]("name")
    val Planet = "planet"

    val saturn: Vertex = graph + (Planet, Name -> "saturn")
    val sun   : Vertex = graph + (Planet, Name -> "sun")*/

    val g = graph.traversal

    println(graph.V.count.head)
    println(graph.E.count.head)
    println(g.V.values("name").toList)

    cluster.close()

  }
}


/*  def main(args: Array[String]): Unit = {
    // create graph
    val graph = JanusGraphFactory.open("inmemory")
    val g = graph.traversal()

    // create graph schema
    var mgmt = graph.openManagement()
    val person = mgmt.makeVertexLabel("person").make()
    val name = mgmt.makePropertyKey("name").dataType(classOf[String]).make()
    val nameIndex = mgmt.buildIndex("nameIndex", classOf[Vertex]).addKey(name).buildCompositeIndex()
    val age = mgmt.makePropertyKey("age").dataType(classOf[Integer]).make()
    val ageIndex = mgmt.buildIndex("ageIndex", classOf[Vertex]).addKey(age).buildCompositeIndex()
    val weight = mgmt.makePropertyKey("weight").dataType(classOf[java.lang.Double]).make()
    val weightIndex = mgmt.buildIndex("weightIndex", classOf[Vertex]).addKey(weight).buildCompositeIndex()
    val defeated = mgmt.makeEdgeLabel("defeated").make()
    val year = mgmt.makePropertyKey("year").dataType(classOf[Integer]).make()
    val yearIndex = mgmt.buildIndex("yearIndex", classOf[Edge]).addKey(year).buildCompositeIndex()
    val score = mgmt.makePropertyKey("score").dataType(classOf[java.lang.Double]).make()
    val scoreIndex = mgmt.buildIndex("scoreIndex", classOf[Edge]).addKey(score).buildCompositeIndex()
    mgmt.commit()

    // verify index status
    mgmt = graph.openManagement()
    println( mgmt.getGraphIndex("nameIndex").getIndexStatus(mgmt.getPropertyKey("name")) )
    println( mgmt.getGraphIndex("ageIndex").getIndexStatus(mgmt.getPropertyKey("age")) )
    println( mgmt.getGraphIndex("weightIndex").getIndexStatus(mgmt.getPropertyKey("weight")) )
    println( mgmt.getGraphIndex("yearIndex").getIndexStatus(mgmt.getPropertyKey("year")) )
    println( mgmt.getGraphIndex("scoreIndex").getIndexStatus(mgmt.getPropertyKey("score")) )
    mgmt.rollback();

    // add vertices and edges
    val tyson = g.addV("person").property("name", "ironmike").property("age", 51).property("weight", 240.5d).next()
    val holyfield = g.addV("person").property("name", "realdeal").property("age", 55).property("weight", 226.0d).next()
    g.V(tyson).as("m").V(holyfield).addE("defeated").property("year", 1996).property("score", 8.5d).to("m").iterate()
    g.V(tyson).as("m").V(holyfield).addE("defeated").property("year", 1997).property("score", 9.7d).to("m").iterate()

    // get vertex by string index
    println( g.V().has("name", "ironmike").valueMap(true).toList() )

    // get vertex by integer index
    println( g.V().has("age", 55).valueMap(true).toList() )

    // get vertex by double index
    println( g.V().has("weight", 240.5d).valueMap(true).toList() )

    // get edge by integer index
    println( g.E().has("year", 1996).valueMap(true).toList() )

    // get edge by double index
    println( g.E().has("score", 9.7d).valueMap(true).toList() )

    // close graph
    graph.close()
  }*/