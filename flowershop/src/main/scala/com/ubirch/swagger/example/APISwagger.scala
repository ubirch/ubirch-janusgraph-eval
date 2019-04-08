package com.ubirch.swagger.example

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}


class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object RestApiInfo extends ApiInfo(
  "The Ubirch API",
  "Docs for the Ubirch REST API",
  "http://ubirch.de",
  "benoit.george@ubirch.com",
  "MIT",
  "http://opensource.org/licenses/MIT")

class ApiSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", RestApiInfo)