import com.ubirch.swagger.example._
import org.scalatra.LifeCycle
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new ApiSwagger

  override def init(context: ServletContext) {
    //context.mount(new ApiController, "/api/*", "API")
    context.mount(new APIJanusController, "/JG/*", "Janus")
    context.mount(new ResourcesApp, "/api-docs")
    context.initParameters("org.scalatra.cors.allowedOrigins") = "http://0.0.0.0"

  }
}

