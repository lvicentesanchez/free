package services.modules

import scalaz._

package object interpreter {
  object async {
    object queue extends QueueAsyncInterpreterInstance
    object stdio extends StdIOAsyncInterpreterInstance
    object timer extends TimerAsyncInterpreterInstance
    object users extends UsersAsyncInterpreterInstance
    object value extends ValueAsyncInterpreterInstance

    object all
      extends QueueAsyncInterpreterInstance
      with StdIOAsyncInterpreterInstance
      with TimerAsyncInterpreterInstance
      with UsersAsyncInterpreterInstance
      with ValueAsyncInterpreterInstance
  }

  object blocking {
    object queue extends QueueBlockingInterpreterInstance
    object stdio extends StdIOBlockingInterpreterInstance
    object timer extends TimerBlockingInterpreterInstance
    object users extends UsersBlockingInterpreterInstance
    object value extends ValueBlockingInterpreterInstance

    object all
      extends QueueBlockingInterpreterInstance
      with StdIOBlockingInterpreterInstance
      with TimerBlockingInterpreterInstance
      with UsersBlockingInterpreterInstance
      with ValueBlockingInterpreterInstance
  }

  implicit def coproductInterpreter[F[_]: ({ type L[M[_]] = ~>[M, N] })#L, G[_]: ({ type L[M[_]] = ~>[M, N] })#L, N[_]]: ~>[({ type L[A] = Coproduct[F, G, A] })#L, N] =
    new ~>[({ type L[A] = Coproduct[F, G, A] })#L, N] {
      def apply[A](input: Coproduct[F, G, A]) = input.run match {
        case -\/(fa) ⇒ implicitly[~>[F, N]].apply(fa)
        case \/-(ga) ⇒ implicitly[~>[G, N]].apply(ga)
      }
    }

  implicit class ImplicitInterpreter[F[_], A](val free: Free.FreeC[F, A]) {
    def runI[M[_]](implicit M: Monad[M], f: F ~> M): M[A] =
      Free.runFC[F, M, A](free)(f)
  }
}
