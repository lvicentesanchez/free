package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

case class QueueID(repr: String)

sealed trait QueueModule[A]

final case class Put[A](queue: QueueID, value: String, f: (⇒ Unit) ⇒ A) extends QueueModule[A]
final case class Get[A](queue: QueueID, f: (⇒ Option[String]) ⇒ A) extends QueueModule[A]

trait QueueInstances {
  implicit val queueAlgebraFunctor: Functor[QueueModule] = new Functor[QueueModule] {
    override def map[A, B](a: QueueModule[A])(f: A ⇒ B) = a match {
      case Put(queue, value, g) ⇒ Put(queue, value, a ⇒ f(g(a)))
      case Get(queue, g) ⇒ Get(queue, a ⇒ f(g(a)))
    }
  }
}

trait QueueFunctions extends InjectFunctions {
  def get[F[_]: Functor](queueID: QueueID)(implicit I: Inject[QueueModule, F]): Free[F, Option[String]] =
    inject[F, QueueModule, Option[String]](Get(queueID, Free.pure))

  def put[F[_]: Functor](queueID: QueueID, value: String)(implicit I: Inject[QueueModule, F]): Free[F, Unit] =
    inject[F, QueueModule, Unit](Put(queueID, value, Free.pure))
}