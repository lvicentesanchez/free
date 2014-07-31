package services.modules
package interpreter

import scala.collection.mutable
import scalaz._
import scalaz.Id._

object QueueIdInterpreter {
  val mem: mutable.Queue[String] = new mutable.Queue[String]()
  val exe: QueueModule ~> Id = new (QueueModule ~> Id) {
    def apply[A](io: QueueModule[A]): Id[A] = io match {
      case Put(_, value, f) ⇒
        mem += value
        f(())

      case Get(_, f) ⇒
        f(mem.dequeueFirst(_ ⇒ true))
    }
  }

  import queue._

  def apply[A](io: Free[QueueModule, A]): Id[A] =
    io.runM[Id](exe.apply[Free[QueueModule, A]])
}

