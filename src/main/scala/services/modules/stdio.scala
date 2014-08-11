package services.modules

import scalaz.{ Free, Inject, InjectFunctions }

object StdIO {
  sealed trait Module[A]

  final case class Get[A](prompt: String, f: String ⇒ A) extends Module[A]
  final case class Put[A](output: String, f: Unit ⇒ A) extends Module[A]
}

trait StdIOFunctions extends InjectFunctions {
  def get[F[_]](prompt: String)(implicit I: Inject[StdIO.Module, F]): Free.FreeC[F, String] =
    Free.liftFC(I.inj(StdIO.Get(prompt, identity)))

  def put[F[_]](output: String)(implicit I: Inject[StdIO.Module, F]): Free.FreeC[F, Unit] =
    Free.liftFC(I.inj(StdIO.Put(output, identity)))
}