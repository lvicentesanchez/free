package services.modules.interpreter

import cats.Id
import services.modules.queue._

import scala.collection.mutable

trait QueueBlockingInterpreterInstance {
  implicit val queueBlockingInterpreterInstance: Blocking[QueueOp] = new QueueBlockingInterpreter {}
}

trait QueueBlockingInterpreter extends Blocking[QueueOp] {
  private val mem: mutable.Queue[String] = new mutable.Queue[String]()

  override def apply[A](input: QueueOp[A]): Id[A] = input match {
    case Pop => mem.dequeueFirst(_ => true)

    case Put(value) => mem += value; ()
  }
}

object QueueBlockingInterpreter extends QueueBlockingInterpreter
