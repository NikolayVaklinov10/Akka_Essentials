package part4faulttolerence

import akka.actor.{Actor, ActorSystem}

object StartingStoppingActors extends App {

  val system = ActorSystem("StoppingActorsDemo")

  object Parent {
    case class StartChild(name: String)
    case class StopChild(name: String)
    case object Stop
  }

  class Parent extends Actor {
    override def receive: Receive = ???
  }

  class Child extends Actor {
    override def receive: Receive = ???
  }

}
