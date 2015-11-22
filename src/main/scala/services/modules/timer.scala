package services.modules

import cats.free.{ Free, Inject }

object Timer {
  sealed trait Module[A]

  final case object Get extends Module[Long]
}

trait TimerFunctions {
  def get[M[_]]()(implicit I: Inject[Timer.Module, M]): Free[M, Long] =
    Free.inject[Timer.Module, M](Timer.Get)
}
