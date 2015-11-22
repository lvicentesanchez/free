package services.modules

import cats.free.{ Free, Inject }

trait QueueModule {

  case class QueueID(repr: String)

  sealed trait QueueOperations[A]

  final case class Get(queue: QueueID) extends QueueOperations[Option[String]]
  final case class Put(queue: QueueID, value: String) extends QueueOperations[Unit]

  trait Queue[M[_]] {

    implicit def I: Inject[QueueOperations, M]

    def get(queueID: QueueID): Free[M, Option[String]] =
      Free.inject[QueueOperations, M](Get(queueID))

    def put(queueID: QueueID, value: String): Free[M, Unit] =
      Free.inject[QueueOperations, M](Put(queueID, value))
  }
}
