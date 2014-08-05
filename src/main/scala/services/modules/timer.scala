package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

object Timer {
  sealed trait Module[A]
  final case class Get[A](f: Long ⇒ A) extends Module[A]
}

trait TimerInstances {
  implicit val timerAlgebraFunctor: Functor[Timer.Module] = new Functor[Timer.Module] {
    override def map[A, B](a: Timer.Module[A])(f: A ⇒ B) = a match {
      case Timer.Get(g) ⇒ Timer.Get(a ⇒ f(g(a)))
    }
  }
}

trait TimerFunctions extends InjectFunctions {
  def get[F[_]: Functor]()(implicit I: Inject[Timer.Module, F]): Free[F, Long] =
    Free.liftF(I.inj(Timer.Get(identity)))
}