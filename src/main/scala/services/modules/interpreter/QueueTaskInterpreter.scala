package services.modules
package interpreter

import scala.collection.mutable
import scalaz._
import scalaz.concurrent.Task

object QueueTaskInterpreter {
  val mem: mutable.Queue[String] = new mutable.Queue[String]()
  val exe: QueueModule ~> Task = new (QueueModule ~> Task) {
    def apply[A](io: QueueModule[A]): Task[A] = io match {
      case Put(_, value, f) ⇒
        mem += value
        Task.delay(f(()))

      case Get(_, f) ⇒
        Task.delay(f(mem.dequeueFirst(_ ⇒ true)))
    }
  }

  import queue._

  def apply[A](io: Free[QueueModule, A]): Task[A] =
    io.runM[Task](exe.apply[Free[QueueModule, A]])
}
