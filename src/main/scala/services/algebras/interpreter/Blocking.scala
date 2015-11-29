package services.algebras.interpreter

import cats.{ Id, ~> }

trait Blocking[F[_]] extends (F ~> Id)
