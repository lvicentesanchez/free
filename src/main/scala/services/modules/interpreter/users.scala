package services.modules.interpreter

import scalaz.Id._
import scalaz.std.option._
import services.modules.{ User, Users }

trait UsersBlockingInterpreterInstance {
  implicit val usersBlockingInterpreterInstance: Blocking[Users.Module] = new Blocking[Users.Module] {
    override def apply[A](input: Users.Module[A]): Id[A] = input match {
      case Users.FindById(uid, f) ⇒
        f(some(User(uid, uid.repr.reverse, 23)))
    }
  }
}
