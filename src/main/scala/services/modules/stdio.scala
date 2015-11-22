package services.modules

import cats.free.{ Free, Inject }

object stdio {

  sealed trait StdIOOp[A]

  final case class Get(prompt: String) extends StdIOOp[String]
  final case class Put(output: String) extends StdIOOp[Unit]

  trait StdIO[M[_]] {

    implicit def I: Inject[StdIOOp, M]

    def get(prompt: String): Free[M, String] =
      Free.inject[StdIOOp, M](Get(prompt))

    def put(output: String): Free[M, Unit] =
      Free.inject[StdIOOp, M](Put(output))
  }

  final class StdIOModule[M[_]](override val I: Inject[StdIOOp, M]) extends StdIO[M]

  object StdIOModule {
    def apply[M[_]](I: Inject[StdIOOp, M]): StdIOModule[M] = new StdIOModule(I)
  }
}
