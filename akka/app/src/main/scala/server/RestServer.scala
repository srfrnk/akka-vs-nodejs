package server

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.mashupbots.socko.webserver.{WebServerConfig, WebServer}
import org.mashupbots.socko.routes._
import calc.FactorialActor
import calc.FibonacciActor
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{ExecutionContext, duration}
import java.util.concurrent.TimeUnit
import ExecutionContext.Implicits.global
import scala.util.Success
import org.jboss.netty.channel.ChannelFutureListener
import org.mashupbots.socko.events.HttpRequestEvent

class RestServer {

  def run (args: Array[String]): Unit = {
    lazy val servicePort = ConfigFactory.load().getInt("service.port")
    lazy val hostname = ConfigFactory.load().getString("service.hostname")

    val actorSystem = ActorSystem("RESTServer")

    val routes = Routes {
      case HttpRequest(request) => request match {
        case GET(PathSegments(action :: Nil)) & QueryString(param : String) if action.equals("factorial") =>
          actorSystem.actorOf(Props[FactorialActor]) ! (BigInt(param.split('=')(1)), request)
        case GET(PathSegments(action :: Nil)) & QueryString(param : String) if action.equals("fibonacci") =>
          actorSystem.actorOf(Props[FibonacciActor]) ! (BigInt(param.split('=')(1)), request)
        case GET(PathSegments(action :: Nil)) if action.equals("id") =>
          request.response.write("Akka")
        case _ =>
          request.response.write("OK")
      }
    }

    val webServer = new WebServer(WebServerConfig(hostname=hostname,port = servicePort),
      routes,
      actorSystem
    )

    webServer.start()

    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run { webServer.stop() }
    })
  }
}
