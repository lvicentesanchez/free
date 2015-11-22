package services.modules.interpreter

import scalaz.concurrent.Task
import scalaz.~>

trait Asynchronous[F[_]] extends (F ~> Task)
