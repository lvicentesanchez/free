package services.modules.interpreter

import cats.Id
import services.modules.Queue

import scala.collection.mutable

trait QueueBlockingInterpreterInstance {
  implicit val queueBlockingInterpreterInstance: Blocking[Queue.Module] = new Blocking[Queue.Module] {
    override def apply[A](input: Queue.Module[A]): Id[A] = input match {
      case Queue.Put(_, value) ⇒
        mem += value
        ()

      case Queue.Get(_) ⇒
        mem.dequeueFirst(_ ⇒ true)
    }

    private val mem: mutable.Queue[String] = new mutable.Queue[String]()
  }
}
