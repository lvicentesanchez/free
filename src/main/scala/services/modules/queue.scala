package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

case class QueueID(repr: String)

object Queue {
  sealed trait Module[A]

  final case class Put[A](queue: QueueID, value: String, f: Unit ⇒ A) extends Module[A]
  final case class Get[A](queue: QueueID, f: Option[String] ⇒ A) extends Module[A]

}

trait QueueInstances {
  implicit val queueAlgebraFunctor: Functor[Queue.Module] = new Functor[Queue.Module] {
    override def map[A, B](a: Queue.Module[A])(f: A ⇒ B) = a match {
      case Queue.Put(queue, value, g) ⇒ Queue.Put(queue, value, a ⇒ f(g(a)))
      case Queue.Get(queue, g) ⇒ Queue.Get(queue, a ⇒ f(g(a)))
    }
  }
}

trait QueueFunctions extends InjectFunctions {
  def get[F[_]: Functor](queueID: QueueID)(implicit I: Inject[Queue.Module, F]): Free[F, Option[String]] =
    Free.liftF(I.inj(Queue.Get(queueID, identity)))

  def put[F[_]: Functor](queueID: QueueID, value: String)(implicit I: Inject[Queue.Module, F]): Free[F, Unit] =
    Free.liftF(I.inj(Queue.Put(queueID, value, identity)))
}