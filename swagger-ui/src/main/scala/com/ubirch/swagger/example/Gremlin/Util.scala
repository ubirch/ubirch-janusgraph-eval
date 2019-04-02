package com.ubirch.swagger.example.Gremlin

import gremlin.scala._
import com.ubirch.swagger.example.Structure.VertexStruct
import org.slf4j.{Logger, LoggerFactory}

class Util {

  def createVertex(label: String, listNames: List[String], listValues: List[String]): VertexStruct = {
    var map: Map[String, String] = Map()
    assert(listNames.length == listValues.length)
    for(i <- listNames.indices){
      map += listNames(i) -> listValues(i)
    }
    val vertex: VertexStruct = new VertexStruct(label, map)
    vertex
  }

}


