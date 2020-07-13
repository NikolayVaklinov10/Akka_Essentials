package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChildActors.Parent.{CreateChild, TellChild}

object ChildActors extends App {
  // beginning of the child actor code

  // companion object for parent
  object Parent {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }

  class Parent extends Actor {
    import Parent._


    override def receive: Receive = {
      case CreateChild(name) =>
        println(s"${self.path} creating child")
        // create a new actor right HERE
        val childRef = context.actorOf(Props[Child], name)
        context.become(withChild(childRef))

    }

    def withChild(childRef: ActorRef):Receive = {
      case TellChild(message) => childRef forward message
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path} I got: $message")
    }
  }

  // initializing actor system
  val system = ActorSystem("ParentChildDemo")
  val parent = system.actorOf(Props[Parent], "parent")
  parent ! CreateChild("child")
  parent ! TellChild("hey Kid!")

  // actor's hierarchies
  // parent -> child -> grandChild
  //        -> child2 ->

  /*
    Guardian actors (top-level)
    - /system = system guardian
    - /user = user-level guardian
    - / = the root guardian
   */

  /**
   * Actor selection
   */
  val childSelection = system.actorSelection("/user/parent/child")
  childSelection ! "I found you!"

  /**
   * Danger!
   *
   * NEVER PASS MUTABLE ACTOR STATE, OR TH `THIS` REFERENCE TO CHILD ACTORS.
   *
   * NEVER IN YOUR LIFE.
   */
}
