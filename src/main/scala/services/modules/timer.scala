package services.modules

import scalaz.{ Free, Inject, InjectFunctions }

object Timer {
  sealed trait Module[A]
  final case class Get[A](f: Long ⇒ A) extends Module[A]
}

trait TimerFunctions extends InjectFunctions {
  def get[F[_]]()(implicit I: Inject[Timer.Module, F]): Free.FreeC[F, Long] =
    Free.liftFC(I.inj(Timer.Get(identity)))
}