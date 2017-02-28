package services.modules

import cats.{ Monad, ~> }
import cats.data.Coproduct
import cats.free.Free
import cats.instances.either._

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

  implicit def PartialCoproductInterpreter[F[_]: ({ type L[M[_]] = M ~> N })#L, G[_]: ({ type L[M[_]] = M ~> N })#L, N[_]]: ({ type L[A] = Coproduct[F, G, A] })#L ~> N =
    new (({ type L[A] = Coproduct[F, G, A] })#L ~> N) {
      def apply[A](input: Coproduct[F, G, A]) = input.run match {
        case Left(fa) ⇒ implicitly[F ~> N].apply(fa)
        case Right(ga) ⇒ implicitly[G ~> N].apply(ga)
      }
    }
}
