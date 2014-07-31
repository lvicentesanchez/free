package services.modules.interpreter

import scalaz.Id._
import scalaz.std.option._
import services.modules._

class UsersBlocking extends Blocking[UsersModule] {
  override def apply[A](input: UsersModule[A]): Id[A] = input match {
    case FindById(uid, f) ⇒
      f(some(User(uid, "name", 23)))
  }
}
