package services.modules.interpreter

import scala.collection.mutable
import scalaz.Id._
import scalaz.concurrent.Task
import services.modules.Queue

trait QueueAsyncInterpreterInstance {
  implicit val queueAsyncInterpreterInstance: Asynchronous[Queue.Module] = new Asynchronous[Queue.Module] {
    override def apply[A](input: Queue.Module[A]): Task[A] = input match {
      case Queue.Put(_, value, f) ⇒
        for {
          _ ← Task.delay(mem += value)
        } yield f(())

      case Queue.Get(_, f) ⇒
        Task.delay(f(mem.dequeueFirst(_ ⇒ true)))
    }

    private val mem: mutable.Queue[String] = new mutable.Queue[String]()
  }
}

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
