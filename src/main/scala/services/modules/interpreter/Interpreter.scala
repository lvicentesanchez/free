package services.modules.interpreter

import scalaz.{ ~>, Monad, Free }

sealed trait Interpreter[F[_], M[_]] {
  def apply[A](input: Free.FreeC[F, A])(implicit M: Monad[M], f: F ~> M): M[A] =
    Free.runFC[F, M, A](input)(f)
}

object Interpreter {
  def apply[F[_], M[_]]: Interpreter[F, M] = new Interpreter[F, M] {}
}