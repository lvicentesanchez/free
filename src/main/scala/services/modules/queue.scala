package services.modules

import scalaz.{ Free ⇒ F, Inject, InjectFunctions }

case class QueueID(repr: String)

object Queue {
  sealed trait Module[A]

  final case class Get(queue: QueueID) extends Module[Option[String]]
  final case class Put(queue: QueueID, value: String) extends Module[Unit]
}

trait QueueFunctions extends InjectFunctions {
  def get[M[_]](queueID: QueueID)(implicit I: Inject[Queue.Module, M]): F.FreeC[M, Option[String]] =
    F.liftFC(I.inj(Queue.Get(queueID)))

  def put[M[_]](queueID: QueueID, value: String)(implicit I: Inject[Queue.Module, M]): F.FreeC[M, Unit] =
    F.liftFC(I.inj(Queue.Put(queueID, value)))
}