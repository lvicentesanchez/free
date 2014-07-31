package services.modules.interpreter

import scalaz.Id._
import scalaz.std.option._
import services.modules.{ User, FindById, UsersModule }

trait UsersBlockingInterpreterInstance {
  implicit val usersBlockingInterpreterInstance: Blocking[UsersModule] = new Blocking[UsersModule] {
    override def apply[A](input: UsersModule[A]): Id[A] = input match {
      case FindById(uid, f) ⇒
        f(some(User(uid, uid.repr.reverse, 23)))
    }
  }
}
