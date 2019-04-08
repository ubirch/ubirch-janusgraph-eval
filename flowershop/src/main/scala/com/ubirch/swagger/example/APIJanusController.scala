package com.ubirch.swagger.example

import com.ubirch.swagger.example.Gremlin.GremlinConnection
import com.ubirch.swagger.example.Gremlin.Util
import com.ubirch.swagger.example.Structure.VertexStruct
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.{ResponseMessage, Swagger, SwaggerSupport}
import org.scalatra.{CorsSupport, ScalatraServlet}
import org.slf4j.{Logger, LoggerFactory}

class APIJanusController(implicit val swagger: Swagger) extends ScalatraServlet
  with NativeJsonSupport with SwaggerSupport with CorsSupport {

  def log : Logger = LoggerFactory.getLogger(this.getClass)
  def ut : Util = new Util

  // Allows CORS support to display the swagger UI when using the same network
  options("/*") {
    response.setHeader(
      "Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers")
    )
  }


  // Stops the APIJanusController from being abstract
  protected val applicationDescription = "The API working with JanusGraph, allows add / display of vertexes/edges"

  // Sets up automatic case class to JSON output serialization
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }


  val addToJanus =
    (apiOperation[VertexStruct]("addToJanusTwoVertexes")
      summary "Add two to JanusGraph"
      description "Just here to test JanusGraph"
      parameters(
      pathParam[Int]("id1").description("id of the first vertex"),
      queryParam[Option[List[String]]]("properties1").description("Properties of the second vertex"),
      pathParam[Int]("id2").description("id of the second vertex"),
      queryParam[Option[List[String]]]("properties2").description("Properties of the second vertex")
    )
      )

  post("/addVertexToJG/:id1/:id2", operation(addToJanus)) {
    log.info("***** coucou1")
    val truc = new CommunicationJanusgraph
    params.get("properties1") match{
      case Some(stuff) => GremlinConnection.addTwoVertexes(params.get("properties1").get, params("id1").toInt, params.get("properties2").get, params("id2").toInt )
      //case Some(stuff) => truc.addVertex(params.get("name1").get, params("id1").toInt, params.get("name2").get, params("id2").toInt)
      case None =>
    }
  }


  val getVertexesJanusGraph =
    (apiOperation[List[VertexStruct]]("getVertexesJanusGraph")
      summary "Show the graph database"
      description "see summary"
      parameter queryParam[Option[Int]]("id").description("Id of the vertex we're looking for")
      responseMessage ResponseMessage(404, "404: Can't find edge with the ID: idNumber")
      )

  get("/getVertexesJG", operation(getVertexesJanusGraph)) {
    val truc = new CommunicationJanusgraph
    params.get("id") match {
      case Some(id) =>{
        val vertex = Gremlin.GremlinConnection.getVertexById(id.toInt);
        if(vertex == null){
          halt(404,  s"404: Can't find vertex with the ID: $id")
        } else{
          vertex.toJson
        }
      }
      case None =>
        val listVertexes = Gremlin.GremlinConnection.getAllVertexes;
        ut.arrayVertexToJson(listVertexes)
    }
  }



}