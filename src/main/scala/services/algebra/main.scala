package services.algebra

import scalaz.Free
import services.algebra.interpreter.QueueTestInterpreter

object main extends App {
  import queue._

  val prg: Free[QueueAlgebra, Seq[String]] =
    for {
      _ ← queue.put(QueueID("myqueue"), "Elephant")
      _ ← queue.put(QueueID("myqueue"), "Donkey")
      value1 ← queue.get(QueueID("myqueue"))
      value2 ← queue.get(QueueID("myqueue"))
    } yield Seq(value1, value2).flatten

  println(QueueTestInterpreter(prg))
}
