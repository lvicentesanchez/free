package services.modules

import scalaz.{ Free, Inject, InjectFunctions }

object Value {
  sealed trait Module[A]

  final case class Pure[A](v: A, f: A ⇒ A) extends Module[A]
}

trait ValueFunctions extends InjectFunctions {
  def pure[F[_], A](pure: A)(implicit I: Inject[Value.Module, F]): Free.FreeC[F, A] =
    Free.liftFC(I.inj(Value.Pure(pure, identity)))
}