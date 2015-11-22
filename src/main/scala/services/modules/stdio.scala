package services.modules

import scalaz.{ Free, Inject }

object StdIO {
  sealed trait Module[A]

  final case class Get(prompt: String) extends Module[String]
  final case class Put(output: String) extends Module[Unit]
}

trait StdIOFunctions {
  def get[M[_]](prompt: String)(implicit I: Inject[StdIO.Module, M]): Free[M, String] =
    Free.liftF(I.inj(StdIO.Get(prompt)))

  def put[M[_]](output: String)(implicit I: Inject[StdIO.Module, M]): Free[M, Unit] =
    Free.liftF(I.inj(StdIO.Put(output)))
}
