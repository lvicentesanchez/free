package services.modules.interpreter

import scalaz.{ ~>, Monad, Free, Functor }

trait Interpreter[F[_], M[_]] {
  def apply[A](input: Free[F, A])(implicit F: Functor[F], M: Monad[M], N: ~>[F, M]): M[A] =
    input.runM[M](N.apply[Free[F, A]])
}