package services.modules

import scalaz.{ Free ⇒ F, Inject, InjectFunctions }

object Timer {
  sealed trait Module[A]

  final case object Get extends Module[Long]
}

trait TimerFunctions extends InjectFunctions {
  def get[M[_]]()(implicit I: Inject[Timer.Module, M]): F.FreeC[M, Long] =
    F.liftFC(I.inj(Timer.Get))
}