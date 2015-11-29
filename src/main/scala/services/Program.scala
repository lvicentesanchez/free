package services

import cats.free.Free
import cats.{ Monad, ~> }
import services.algebras.interpreter._

/**
 * Created by luissanchez on 29/11/2015.
 */
abstract class Program[M[_], A](implicit M: Monad[M]) {

  type Frg[_]
  type Prg = Free[Frg, A]

  def I: Frg ~> M

  def program(): M[A] = receipt().interpret[M](M, I)

  def receipt(): Prg
}
