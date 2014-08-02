package services.modules.interpreter

import scalaz.~>
import scalaz.concurrent.Task

trait Asynchronous[F[_]] extends (F ~> Task)
