package services.modules.interpreter

import scala.collection.mutable
import scalaz.Id._
import services.modules.Queue

trait QueueBlockingInterpreterInstance {
  implicit val queueBlockingInterpreterInstance: Blocking[Queue.Module] = new Blocking[Queue.Module] {
    override def apply[A](input: Queue.Module[A]): Id[A] = input match {
      case Queue.Put(_, value, f) ⇒
        mem += value
        f(())

      case Queue.Get(_, f) ⇒
        f(mem.dequeueFirst(_ ⇒ true))
    }

    private val mem: mutable.Queue[String] = new mutable.Queue[String]()
  }
}
