package services.algebra
package interpreter

import scala.collection.mutable
import scalaz._
import scalaz.Id._

object QueueTestInterpreter {
  val mem: mutable.Queue[String] = new mutable.Queue[String]()
  val exe: QueueAlgebra ~> Id = new (QueueAlgebra ~> Id) {
    def apply[A](io: QueueAlgebra[A]): Id[A] = io match {
      case Put(_, value, f) ⇒
        mem += value
        f(())

      case Get(_, f) ⇒
        f(mem.dequeueFirst(_ ⇒ true))
    }
  }

  import queue._

  def apply[A](io: Free[QueueAlgebra, A]): Id[A] =
    io.runM[Id](exe.apply[Free[QueueAlgebra, A]])
}
