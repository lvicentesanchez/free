package services.modules.interpreter

import scalaz.Id._
import scalaz.~>

trait Blocking[F[_]] extends (F ~> Id)
