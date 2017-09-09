package calc

import akka.actor.Actor
import org.mashupbots.socko.events.HttpRequestEvent
import scala.annotation.tailrec
import scala.math

class FibonacciActor extends Actor {
  override def receive: Actor.Receive = {
    case (n : BigInt, request : HttpRequestEvent) =>
      request.response.write(fibonacci(n).toString)
      context.stop(self)
  }

  private def fibonacci (count : BigInt) : BigInt = {
    count match {
      case a if a < 2 => 1
      case _@a => fibonacci(a - 1) + fibonacci(a-2)
    }
  }
}
