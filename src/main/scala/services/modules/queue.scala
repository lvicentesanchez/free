package services.modules

import scalaz.{ Free, Inject }

case class QueueID(repr: String)

object Queue {
  sealed trait Module[A]

  final case class Get(queue: QueueID) extends Module[Option[String]]
  final case class Put(queue: QueueID, value: String) extends Module[Unit]
}

trait QueueFunctions {
  def get[M[_]](queueID: QueueID)(implicit I: Inject[Queue.Module, M]): Free[M, Option[String]] =
    Free.liftF(I.inj(Queue.Get(queueID)))

  def put[M[_]](queueID: QueueID, value: String)(implicit I: Inject[Queue.Module, M]): Free[M, Unit] =
    Free.liftF(I.inj(Queue.Put(queueID, value)))
}
