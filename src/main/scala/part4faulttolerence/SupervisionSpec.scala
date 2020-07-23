package part4faulttolerence

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class SupervisionSpec  extends TestKit(ActorSystem("SupervisionSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import SupervisionSpec._

  "A supervisor" should {
    "resume its child in case of minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      child ! "I love Akka"
      child ! Report
      expectMsg(3)
    }
  }

}

object SupervisionSpec {

  class Supervisor extends Actor {

    override val supervisorStrategy = OneForOneStrategy() {
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: RuntimeException => Resume
      case _: Exception => Escalate
    }

    override def receive: Receive = {
      case props: Props =>
        val childRef = context.actorOf(props)
        sender() ! childRef
    }
  }

  case object Report
  class FussyWordCounter extends Actor {
    var words = 0

    override def receive: Receive = {
      case "" => throw new NullPointerException("sentence is empty")
      case sentence: String =>
        if (sentence.length > 20) throw new RuntimeException("sentence is too big")
        else if (!Character.isUpperCase(sentence(0))) throw new IllegalArgumentException("sentence must start with uppercase")
        else words += sentence.split(" ").length
      case _ => throw new Exception("can only receive strings")
    }
  }
}