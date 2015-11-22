package services.modules.interpreter

import cats.Id
import services.modules.queue._

import scala.collection.mutable

trait QueueBlockingInterpreterInstance {
  implicit val queueBlockingInterpreterInstance: Blocking[QueueOperations] = new QueueBlockingInterpreter {}
}

trait QueueBlockingInterpreter extends Blocking[QueueOperations] {
  private val mem: mutable.Queue[String] = new mutable.Queue[String]()

  override def apply[A](input: QueueOperations[A]): Id[A] = input match {
    case Put(_, value) ⇒
      mem += value
      ()

    case Get(_) ⇒
      mem.dequeueFirst(_ ⇒ true)
  }
}

object QueueBlockingInterpreter extends QueueBlockingInterpreter
