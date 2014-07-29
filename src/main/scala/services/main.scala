package services

import services.modules.interpreter.QueueTaskInterpreter
import services.modules.{ QueueModule, QueueID, queue }

import scalaz.Free

object main extends App {
  import services.modules.queue._

  val prg: Free[QueueModule, Seq[String]] =
    for {
      _ ← queue.put(QueueID("myqueue"), "Elephant")
      _ ← queue.put(QueueID("myqueue"), "Donkey")
      value1 ← queue.get(QueueID("myqueue"))
      value2 ← queue.get(QueueID("myqueue"))
    } yield Seq(value1, value2).flatten

  println(QueueTaskInterpreter(prg).run)
}
