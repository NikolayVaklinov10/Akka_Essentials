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
      | loglevel = "ERROR"
      | }
      |""".stripMargin

  //
  val config = ConfigFactory.parseString(configString)
  val system = ActorSystem("ConfigurationDemo", ConfigFactory.load(config))
  // instances
  val actor = system.actorOf(Props[SimpleLoggingActor])

  // actor message
  actor ! "A message to remember"

  /**
   * 2 - config file
   */

  val defaultConfigFileSystem = ActorSystem("DefaultConfigFileDemo")
  val defaultConfActor = defaultConfigFileSystem.actorOf(Props[SimpleLoggingActor])
  defaultConfActor ! "Remember me"

  /**
   * 3 - separate config in the same file
   */
  val specialConfig = ConfigFactory.load("mySpecialConfig")
  val specialConfigSystem = ActorSystem("SpecialConfigDemo", specialConfig)
  val specialConfigActor = specialConfigSystem.actorOf(Props[SimpleLoggingActor])
  specialConfigActor ! "Remember me, I'm special"

  /**
   * 4 - separate config in another file
   */

  val separateConfig = ConfigFactory.load("secretFolder/secreteConfiguration.cong")
  println(s"separate config log level: ${separateConfig.getString("akka.loglevel")}")

  /**
   * 5 - different file formats
   * JSON, Properties
   */

  val jsonConfig = ConfigFactory.load("json/jsonConfig.json")
  println(s"json config: ${jsonConfig.getString("aJsonProperty")}")
  println(s"json config: ${jsonConfig.getString("akka.loglevel")}")



}
