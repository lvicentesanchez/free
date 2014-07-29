package services.modules
package interpreter

import scalaz._
import scalaz.Id._
import scalaz.std.option._

object UsersTestInterpreter {
  val exe: UsersModule ~> Id = new (UsersModule ~> Id) {
    def apply[A](io: UsersModule[A]): Id[A] = io match {
      case FindById(uid, f) ⇒
        f(some(User(uid, "name", 23)))
    }
  }

  import users._

  def apply[A](io: Free[UsersModule, A]): Id[A] =
    io.runM[Id](exe.apply[Free[UsersModule, A]])
}
