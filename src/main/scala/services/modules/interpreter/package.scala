package services.modules

import cats.data.{ Coproduct, Xor }
import cats.free.Free
import cats.{ Monad, ~> }

package object interpreter {
  object blocking {
    object queue extends QueueBlockingInterpreterInstance
    object stdio extends StdIOBlockingInterpreterInstance
    object timer extends TimerBlockingInterpreterInstance

    object all
      extends QueueBlockingInterpreterInstance
      with StdIOBlockingInterpreterInstance
      with TimerBlockingInterpreterInstance
  }

  implicit class InterpreterExtensionMethods[F[_], A](val free: Free[F, A]) extends AnyVal {
    def interpret[M[_]](implicit M: Monad[M], f: F ~> M): M[A] =
      free.foldMap(f)
  }

  implicit def PartialCoproductInterpreter[F[_], G[_], N[_]](implicit FN: F ~> N, GN: G ~> N): Coproduct[F, G, ?] ~> N =
    new ~>[Coproduct[F, G, ?], N] {
      def apply[A](input: Coproduct[F, G, A]) =
        input.run match {
          case Xor.Left(fa) ⇒ FN.apply(fa)
          case Xor.Right(ga) ⇒ GN.apply(ga)
        }
    }
}
