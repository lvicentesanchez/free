package services.modules

import scalaz.{ Free, Inject }

object Timer {
  sealed trait Module[A]

  final case object Get extends Module[Long]
}

trait TimerFunctions {
  def get[M[_]]()(implicit I: Inject[Timer.Module, M]): Free[M, Long] =
    Free.liftF(I.inj(Timer.Get))
}
