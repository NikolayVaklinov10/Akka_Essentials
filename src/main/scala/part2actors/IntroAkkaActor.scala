package part2actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object IntroAkkaActor extends App {

  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  /**
   * 1 - inline configuration
   */
  val configString =
    """
      | akka {
      | loglevel = "DEBUG"
      | }
      |""".stripMargin

  //
  val config = ConfigFactory.parseString(configString)
  val system = ActorSystem("ConfigurationDemo", ConfigFactory.load(config))
  // instances
  val actor = system.actorOf(Props[SimpleLoggingActor])
}
