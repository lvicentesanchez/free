package services.modules

import scalaz.Free

object Value

trait ValueFunctions {
  def pure[M[_], A](pure: A): Free[M, A] =
    Free.pure[M, A](pure)
}
