package services.modules

import cats.free.{ Free, Inject }

trait StdIOModule {

  sealed trait StdIOOperations[A]

  final case class Get(prompt: String) extends StdIOOperations[String]
  final case class Put(output: String) extends StdIOOperations[Unit]

  trait StdIO[M[_]] {

    implicit def I: Inject[StdIOOperations, M]

    def get(prompt: String): Free[M, String] =
      Free.inject[StdIOOperations, M](Get(prompt))

    def put(output: String): Free[M, Unit] =
      Free.inject[StdIOOperations, M](Put(output))
  }
}
