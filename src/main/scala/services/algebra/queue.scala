package services.algebra

import scalaz.{ Free, Inject, InjectFunctions, Functor }

case class QueueID(repr: String)
sealed trait QueueAlgebra[A]

final case class Put[A](queue: QueueID, value: String, f: (⇒ Unit) ⇒ A) extends QueueAlgebra[A]
final case class Get[A](queue: QueueID, f: (⇒ Option[String]) ⇒ A) extends QueueAlgebra[A]

trait QueueInstances {
  implicit val queueAlgebraFunctor: Functor[QueueAlgebra] = new Functor[QueueAlgebra] {
    override def map[A, B](a: QueueAlgebra[A])(f: A ⇒ B) = a match {
      case Put(queue, value, g) ⇒ Put(queue, value, a ⇒ f(g(a)))
      case Get(queue, g) ⇒ Get(queue, a ⇒ f(g(a)))
    }
  }
}

trait QueueFunctions extends InjectFunctions {
  def get[F[_]](queueID: QueueID)(implicit FF: Functor[F], I: Inject[QueueAlgebra, F]): Free[F, Option[String]] =
    inject[F, QueueAlgebra, Option[String]](Get(queueID, Free.pure))

  def put[F[_]](queueID: QueueID, value: String)(implicit FF: Functor[F], I: Inject[QueueAlgebra, F]): Free[F, Unit] =
    inject[F, QueueAlgebra, Unit](Put(queueID, value, Free.pure))
}