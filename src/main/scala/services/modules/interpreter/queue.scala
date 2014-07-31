package services.modules.interpreter

import scala.collection.mutable
import scalaz.Id._
import services.modules.{ QueueModule, Get, Put }

trait QueueBlockingInterpreterInstance {
  implicit val queueBlockingInterpreterInstance: Blocking[QueueModule] = new Blocking[QueueModule] {
    override def apply[A](input: QueueModule[A]): Id[A] = input match {
      case Put(_, value, f) ⇒
        mem += value
        f(())

      case Get(_, f) ⇒
        f(mem.dequeueFirst(_ ⇒ true))
    }

    private val mem: mutable.Queue[String] = new mutable.Queue[String]()
  }
}
