package services.modules

import scalaz.{ Free ⇒ F, Inject, InjectFunctions }

object StdIO {
  sealed trait Module[A]

  final case class Get(prompt: String) extends Module[String]
  final case class Put(output: String) extends Module[Unit]
}

trait StdIOFunctions extends InjectFunctions {
  def get[M[_]](prompt: String)(implicit I: Inject[StdIO.Module, M]): F.FreeC[M, String] =
    F.liftFC(I.inj(StdIO.Get(prompt)))

  def put[M[_]](output: String)(implicit I: Inject[StdIO.Module, M]): F.FreeC[M, Unit] =
    F.liftFC(I.inj(StdIO.Put(output)))
}