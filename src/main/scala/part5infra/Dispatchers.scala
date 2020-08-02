package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Dispatchers extends App {

  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message =>
        count += 1
        log.info(s"[$count] $message")
    }
  }

  val system = ActorSystem("DispatcherDemo", ConfigFactory.load().getConfig("dispatchersDemo"))

  val actors = for (i <- 1 to 10) yield system.actorOf(Props[Counter].withDispatcher("my-dispatcher"), s"counter_$i")

}
