package com.ubirch.swagger.example

import com.ubirch.swagger.example.Gremlin.Util
import com.ubirch.swagger.example.Structure.VertexStruct
import gremlin.scala._
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.{TinkerGraph, TinkerIoRegistryV3d0}
import org.slf4j.{Logger, LoggerFactory}
import shapeless.HList

import scala.collection.mutable.ListBuffer
import scala.util.Try
import gremlin.scala
import org.janusgraph.core.{JanusGraph, JanusGraphFactory}


class CommunicationJanusgraph {

  def log: Logger = LoggerFactory.getLogger(this.getClass)

  def addVertex(nameOfVertex1: String, id1: Int, nameOfVertex2: String, id2: Int): Unit = {
    log.info("addVertex")
    val serializer = new GryoMessageSerializerV3d0(GryoMapper.build.addRegistry(TinkerIoRegistryV3d0.instance))
    val cluster = Cluster.build.addContactPoint("localhost").port(8182).serializer(serializer).create
    implicit val graph: ScalaGraph = EmptyGraph.instance.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))
//    implicit val graph: ScalaGraph = TinkerGraph.open.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))
    /*
        implicit val graph = EmptyGraph.instance.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))
    */

    val Name = Key[String]("name")
    val Id = Key[Int]("id")
    val Planet = "planet"


    val g = graph.traversal

    //val edge = g.V(v1).as("a").V(v2).addE("test").iterate()
    val v1: scala.Vertex = graph + (Planet, Name -> nameOfVertex1, Id -> id1)
    val v2: scala.Vertex = graph + (Planet, Name -> nameOfVertex2, Id -> id2)
    try{
      v1.asScala.addEdge("linked to", v2)
    }
    catch {
      case e: Error => {
        cluster.close()
        log.info("ehegeg")
        "NOK"
        log.error("error", e)
      }
    }


    log.info("normalent c'est ajoute mn coco")

    cluster.close()
    "OK"
  }

  def getVertexes(id: Int = -1): List[VertexStruct] = {

    val serializer = new GryoMessageSerializerV3d0(GryoMapper.build.addRegistry(TinkerIoRegistryV3d0.instance))
    val cluster = Cluster.build.addContactPoint("localhost").port(8182).serializer(serializer).create
    implicit val graph: ScalaGraph = EmptyGraph.instance.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))


    val g = graph.traversal
    val theAssignedId = Key[Int]("id")
    val Name = Key[String]("name")

    val result = if (id == -1) {
      var lis =List(5555)
      lis
      //List(g.V.values("id").toList.map(_.toString.toInt))
    } else {
      //val ourBadBoy = graph + ("planet", theAssignedId -> id.toString)
      /*    g.V(ourBadBoy).values()*/
      val allValues = g.V.toList()
      val listToReturnBuffer = new ListBuffer[Int]()
      try {
        allValues.map { vertex =>
          log.info("*properties and values: " + g.V(vertex).valueMap.toList().toString())
          log.info("*name                 : " + g.V(vertex).value(Name).toList().toString())
          log.info("OK1")
          val vertexValue = g.V(vertex)

          val listIdStr : List[Int] = vertexValue.value(theAssignedId).toList

          log.info("OK1.5")
          val listId:List[Int] = listIdStr.filter(d => Try(d.toInt).isSuccess).map(_.toInt)
          log.info(g.V.toList.toString())
          log.info(listId.toString())
          listId.head
//          log.info("OK2")
          //log.info("listId.getClass.toString    : " + listId.getClass.toString)
//          log.info("listId.toString(): " + listId)
//          log.info(s"listId: ${listId.head}")
//          log.info(s"listId.head.getClass: ${listId.head.getClass}")
//          listId.head
          //log.info("OK3")
          //log.info("*arrayId             : " + maybeItsTheName.toString)
//          listToReturnBuffer += listId.head
          //listId.head.toString
        }
      }
      catch {
        case e: Exception =>
          log.error("error", e)
          var lis = List(5556)
          lis
      }
      finally {
        listToReturnBuffer.toList
      }
      //log.info("coucou2: " + g.V(ourBadBoy).)
    }
    //log.info("g.V.values(\"name\").toList.toString: " + g.V.values("name").toList.toString)

    cluster.close()
    val label = "un ptit label monga"
    val listProp: List[String] = List("1", "deux", "trois", "viva", "algeria")
    val listValues: List[String] = List("fhe", "gjrigfhe", "jhrueg", "hiuefhfe", "hriuoger")
    val ut = new Util
    val vertounet: VertexStruct = ut.createVertex(label, listProp, listValues)
    val listVert = List(vertounet)
    listVert

  }


/*
  def getAllVertexes(): Vertex = {
    val serializer = new GryoMessageSerializerV3d0(GryoMapper.build.addRegistry(TinkerIoRegistryV3d0.instance))
    val cluster = Cluster.build.addContactPoint("localhost").port(8182).serializer(serializer).create
    implicit val graph: ScalaGraph = EmptyGraph.instance.asScala.configure(_.withRemote(DriverRemoteConnection.using(cluster)))


    val g = graph.traversal
    val theAssignedId = Key[Int]("id")
    val Name = Key[String]("name")


    cluster.close()


  }
*/



}