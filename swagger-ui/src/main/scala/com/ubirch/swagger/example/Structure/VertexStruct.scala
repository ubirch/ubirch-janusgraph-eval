package com.ubirch.swagger.example.Structure

case class VertexStruct(label: String, properties: Map[String, String]) {
  override def toString(): String = {
    var s: String = s"Label: ${label}"
    for( (k,v) <- properties) s += s"\n$k: $v"
    s
  }

  def toJson(): String ={
    /*implicit val formats = net.liftweb.json.DefaultFormats
    import net.liftweb.json.JsonAST._
    import net.liftweb.json.Extraction._
    val s: String = compactRender(decompose("label:" + this.label + this.properties.toString))
    s
    */
    val s : String = scala.util.parsing.json.JSONObject(Map("Label", this.label)).toString()
  }
}