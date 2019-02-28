package services.modules

import cats.InjectK
import cats.free.Free

object Timer {
  sealed trait Module[A]

  final case object Get extends Module[Long]
}

trait TimerFunctions {
  def get[M[_]]()(implicit I: InjectK[Timer.Module, M]): Free[M, Long] =
    Free.inject[Timer.Module, M](Timer.Get)
}
