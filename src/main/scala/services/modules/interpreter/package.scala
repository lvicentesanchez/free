package services.modules

import scalaz.{ Coproduct, ~>, -\/, \/- }

package object interpreter {
  object blocking {
    object queue extends QueueBlockingInterpreterInstance
    object stdio extends StdIOBlockingInterpreterInstance
    object timer extends TimerBlockingInterpreterInstance
    object users extends UsersBlockingInterpreterInstance

    object all extends QueueBlockingInterpreterInstance with StdIOBlockingInterpreterInstance with TimerBlockingInterpreterInstance with UsersBlockingInterpreterInstance
  }

  implicit def coproductInterpreter[F[_]: ({ type L[M[_]] = ~>[M, N] })#L, G[_]: ({ type L[M[_]] = ~>[M, N] })#L, N[_]] =
    new ~>[({ type L[A] = Coproduct[F, G, A] })#L, N] {
      def apply[A](input: Coproduct[F, G, A]) = input.run match {
        case -\/(fa) ⇒ implicitly[~>[F, N]].apply(fa)
        case \/-(ga) ⇒ implicitly[~>[G, N]].apply(ga)
      }
    }
}
