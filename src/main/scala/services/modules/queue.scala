package services.modules

import cats.InjectK
import cats.free.Free

case class QueueID(repr: String)

object Queue {
  sealed trait Module[A]

  final case class Get(queue: QueueID) extends Module[Option[String]]
  final case class Put(queue: QueueID, value: String) extends Module[Unit]
}

trait QueueFunctions {
  def get[M[_]](queueID: QueueID)(
      implicit I: InjectK[Queue.Module, M]): Free[M, Option[String]] =
    Free.inject[Queue.Module, M](Queue.Get(queueID))

  def put[M[_]](queueID: QueueID, value: String)(
      implicit I: InjectK[Queue.Module, M]): Free[M, Unit] =
    Free.inject[Queue.Module, M](Queue.Put(queueID, value))
}
