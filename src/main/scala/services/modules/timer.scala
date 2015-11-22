package services.modules

import cats.free.{ Free, Inject }

object timer {
  sealed trait TimerOp[A]

  final case object Get extends TimerOp[Long]

  trait Timer[M[_]] {

    implicit def I: Inject[TimerOp, M]

    val get: Free[M, Long] =
      Free.inject[TimerOp, M](Get)
  }

  final class TimerModule[M[_]](override val I: Inject[TimerOp, M]) extends Timer[M]

  object TimerModule {
    def apply[M[_]](I: Inject[TimerOp, M]): TimerModule[M] = new TimerModule(I)
  }
}
