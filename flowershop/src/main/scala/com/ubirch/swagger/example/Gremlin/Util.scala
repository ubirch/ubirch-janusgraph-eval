package com.ubirch.swagger.example.Gremlin

import com.ubirch.swagger.example.Structure.VertexStruct
import net.liftweb.json.JValue
import org.json4s.JsonAST
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.slf4j.{Logger, LoggerFactory}

class Util {

  def log: Logger = LoggerFactory.getLogger(this.getClass)

  def createVertex(label: String, listNames: List[String], listValues: List[String]): VertexStruct = {
    var map: Map[String, String] = Map()
    assert(listNames.length == listValues.length)
    for(i <- listNames.indices){
      map += listNames(i) -> listValues(i)
    }
    val vertex: VertexStruct = new VertexStruct(label, map)
    vertex
  }

  def listToJson(listString: List[String]): String = {
    val str = listString.toString.substring(5, listString.toString().length - 1)
    str
  }

  def arrayVertexToJson(arrayVertexes: Array[VertexStruct]): String = {

    implicit def vertexes2JValue(v: VertexStruct): JsonAST.JObject = {
      ("label" -> v.label) ~ ("properties" -> v.properties)
    }
    val json = "list of vertexes" -> reformatArrayVertex(arrayVertexes).toList
    implicit val formats = org.json4s.DefaultFormats

    Serialization.write(json)
  }
/*  def valueMapToStr(valueMap: List[String]): String = {
    val truc = valueMap.map()
    val listNames: List[String] = valueMap.map(_.substring(_ contains ))
    val listVa
  }*/

  def reformatArrayVertex(arrayVertex: Array[VertexStruct]): Array[VertexStruct] = {
    val arrayVertexReformated: Array[VertexStruct] = new Array(arrayVertex.length)
    var i = 0
    for(v <- arrayVertex){
      val label = v.label
      val properties: Map[String, String] = v.properties
      var propertiesReformated: Map[String, String] = Map()
      for((key, value) <- properties) propertiesReformated += (key.toString -> value.toString.substring(1, value.length - 1))

      val vertexReformated: VertexStruct = new VertexStruct(label, propertiesReformated)

      arrayVertexReformated(i) = vertexReformated
      i = i + 1
    }


    arrayVertexReformated.foreach(v => log.info(v.toString))
    arrayVertexReformated
  }
}


