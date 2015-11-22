package services.modules.interpreter

import services.modules.Queue

import scala.collection.mutable
import scalaz.Id._
import scalaz.concurrent.Task

trait QueueAsyncInterpreterInstance {
  implicit val queueAsyncInterpreterInstance: Asynchronous[Queue.Module] = new Asynchronous[Queue.Module] {
    override def apply[A](input: Queue.Module[A]): Task[A] = input match {
      case Queue.Put(_, value) ⇒
        for {
          _ ← Task.delay(mem += value)
        } yield ()

      case Queue.Get(_) ⇒
        Task.delay(mem.dequeueFirst(_ ⇒ true))
    }

    private val mem: mutable.Queue[String] = new mutable.Queue[String]()
  }
}

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
