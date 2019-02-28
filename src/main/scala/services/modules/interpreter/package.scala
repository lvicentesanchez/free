package services.modules

import cats.data.EitherK
import cats.free.Free
import cats.{Monad, ~>}

package object interpreter {
  object blocking {
    object queue extends QueueBlockingInterpreterInstance
    object stdio extends StdIOBlockingInterpreterInstance
    object timer extends TimerBlockingInterpreterInstance
    object users extends UsersBlockingInterpreterInstance

    object all
        extends QueueBlockingInterpreterInstance
        with StdIOBlockingInterpreterInstance
        with TimerBlockingInterpreterInstance
        with UsersBlockingInterpreterInstance
  }

  implicit class InterpreterExtensionMethods[F[_], A](val free: Free[F, A]) {
    def runI[M[_]](implicit M: Monad[M], f: F ~> M): M[A] =
      free.foldMap(f)
  }

  implicit def PartialEitherKInterpreter[F[_]: ({ type L[M[_]] = M ~> N })#L,
                                           G[_]: ({ type L[M[_]] = M ~> N })#L,
                                           N[_]]
    : ({ type L[A] = EitherK[F, G, A] })#L ~> N =
    new (({ type L[A] = EitherK[F, G, A] })#L ~> N) {
      def apply[A](input: EitherK[F, G, A]) = input.run match {
        case Left(fa) => implicitly[F ~> N].apply(fa)
        case Right(ga) => implicitly[G ~> N].apply(ga)
      }
    }
}
