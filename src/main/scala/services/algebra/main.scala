package services.algebra

import scala.collection.mutable
import scalaz.Id._
import scalaz.{ ~>, Free }

object TestQueue {
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
object main extends App {
  import queue._

  val prg: Free[QueueAlgebra, Seq[String]] =
    for {
      _ ← queue.put(QueueID("myqueue"), "Elephant")
      _ ← queue.put(QueueID("myqueue"), "Donkey")
      value1 ← queue.get(QueueID("myqueue"))
      value2 ← queue.get(QueueID("myqueue"))
    } yield Seq(value1, value2).flatten

  println(TestQueue(prg))
}
