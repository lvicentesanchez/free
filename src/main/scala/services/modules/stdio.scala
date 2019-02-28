package services.modules

import cats.InjectK
import cats.free.Free

object StdIO {
  sealed trait Module[A]

  final case class Get(prompt: String) extends Module[String]
  final case class Put(output: String) extends Module[Unit]
}

trait StdIOFunctions {
  def get[M[_]](prompt: String)(
      implicit I: InjectK[StdIO.Module, M]): Free[M, String] =
    Free.inject[StdIO.Module, M](StdIO.Get(prompt))

  def put[M[_]](output: String)(
      implicit I: InjectK[StdIO.Module, M]): Free[M, Unit] =
    Free.inject[StdIO.Module, M](StdIO.Put(output))
}
