package services.modules

import scalaz.{ Free ⇒ F, Inject, InjectFunctions }

object StdIO {
  sealed trait Module[A]

  final case class Get[A](prompt: String, f: String ⇒ A) extends Module[A]
  final case class Put[A](output: String, f: Unit ⇒ A) extends Module[A]
}

trait StdIOFunctions extends InjectFunctions {
  def get[M[_]](prompt: String)(implicit I: Inject[StdIO.Module, M]): F.FreeC[M, String] =
    F.liftFC(I.inj(StdIO.Get(prompt, identity)))

  def put[M[_]](output: String)(implicit I: Inject[StdIO.Module, M]): F.FreeC[M, Unit] =
    F.liftFC(I.inj(StdIO.Put(output, identity)))
}