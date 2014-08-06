package services.modules.interpreter

import scalaz.{ ~>, Monad, Free, Functor }

sealed trait Interpreter[F[_], M[_]] {
  def apply[A](input: Free[F, A])(implicit F: Functor[F], M: Monad[M], f: F ~> M): M[A] =
    input.runM[M](f[Free[F, A]])
}

object Interpreter {
  def apply[F[_], M[_]]: Interpreter[F, M] = new Interpreter[F, M] {}
}