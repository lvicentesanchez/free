package services.modules

import scalaz.{ Free, Inject, InjectFunctions }

case class QueueID(repr: String)

object Queue {
  sealed trait Module[A]

  final case class Put[A](queue: QueueID, value: String, f: Unit ⇒ A) extends Module[A]
  final case class Get[A](queue: QueueID, f: Option[String] ⇒ A) extends Module[A]
}

trait QueueFunctions extends InjectFunctions {
  def get[F[_]](queueID: QueueID)(implicit I: Inject[Queue.Module, F]): Free.FreeC[F, Option[String]] =
    Free.liftFC(I.inj(Queue.Get(queueID, identity)))

  def put[F[_]](queueID: QueueID, value: String)(implicit I: Inject[Queue.Module, F]): Free.FreeC[F, Unit] =
    Free.liftFC(I.inj(Queue.Put(queueID, value, identity)))
}