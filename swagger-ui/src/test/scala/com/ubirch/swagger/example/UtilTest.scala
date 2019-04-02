package com.ubirch.swagger.example

import com.ubirch.swagger.example.Gremlin.Util
import com.ubirch.swagger.example.Structure.VertexStruct
import org.slf4j.{Logger, LoggerFactory}

object UtilTest {

  def log: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val label = "un ptit label monga"
    val listProp: List[String] = List("1", "deux", "trois", "viva", "algeria")
    val listValues: List[String] = List("fhe", "gjrigfhe", "jhrueg", "hiuefhfe", "hriuoger")
    val ut = new Util
    val vertounet: VertexStruct = ut.createVertex(label, listProp, listValues)
    log.info(vertounet.toString())
    log.info(scala.util.parsing.json.JSONObject(vertounet.properties).toString())
  }
}
