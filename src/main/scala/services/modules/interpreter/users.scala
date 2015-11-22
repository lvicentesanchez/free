package services.modules.interpreter

import cats.Id
import services.modules.{ User, Users }

trait UsersBlockingInterpreterInstance {
  implicit val usersBlockingInterpreterInstance: Blocking[Users.Module] = new Blocking[Users.Module] {
    override def apply[A](input: Users.Module[A]): Id[A] = input match {
      case Users.FindById(uid) ⇒
        Option(User(uid, uid.repr.reverse, 23))
    }
  }
}
