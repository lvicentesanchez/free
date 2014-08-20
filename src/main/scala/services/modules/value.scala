package services.modules

import scalaz.{ Free ⇒ F }

object Value

trait ValueFunctions {
  def pure[M[_], A](pure: A): F.FreeC[M, A] =
    F.point[({ type L[x] = scalaz.Coyoneda[M, x] })#L, A](pure)
}