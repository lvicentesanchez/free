package services.algebras

import cats.free.{ Free, Inject }

object queue {

  sealed trait QueueOp[A]

  final case object Pop extends QueueOp[Option[String]]
  final case class Put(value: String) extends QueueOp[Unit]

  trait Queue[M[_]] {

    implicit def I: Inject[QueueOp, M]

    val pop: Free[M, Option[String]] =
      Free.inject[QueueOp, M](Pop)

    def put(value: String): Free[M, Unit] =
      Free.inject[QueueOp, M](Put(value))
  }

  final class QueueAlgebra[M[_]](override val I: Inject[QueueOp, M]) extends Queue[M]

  object QueueAlgebra {
    def apply[M[_]](I: Inject[QueueOp, M]): QueueAlgebra[M] = new QueueAlgebra(I)
  }
}
