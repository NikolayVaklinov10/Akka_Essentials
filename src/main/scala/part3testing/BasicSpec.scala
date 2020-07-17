package part3testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import part3testing.BasicSpec.{BlackHole, LabTestActor, SimpleActor}

import scala.concurrent.duration._

class BasicSpec extends TestKit(ActorSystem("BasicSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  // setup
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A simple actor" should {
    "send back the same message" in {
      // testing scenario
      val echoActor = system.actorOf(Props[SimpleActor])
      val message = "hello, test"
      echoActor ! message

      expectMsg(message)  // akka.test.single-expect-default
    }
  }

  "A blackhole actor" should {
    "send back some message" in {
      val blackhole = system.actorOf(Props[BlackHole])
      val message = "hello, test"
      blackhole ! message

      expectNoMessage(1 second)
    }
  }

  // message assertions
  "A lab test actor" should {
    val labTestActor = system.actorOf(Props[LabTestActor])
    "turn a string into uppercase" in {
      labTestActor ! "I love Akka"
      expectMsg("I LOVE AKKA")
    }
  }


}

object BasicSpec {

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }

  class BlackHole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }

  class LabTestActor extends Actor {
    override def receive: Receive = {
      case message: String => sender() ! message.toUpperCase()
    }
  }
}
