package services.modules.interpreter

import services.modules.{ User, Users }

import scalaz.Id._
import scalaz.concurrent.Task
import scalaz.std.option._

trait UsersAsyncInterpreterInstance {
  implicit val usersAsyncInterpreterInstance: Asynchronous[Users.Module] = new Asynchronous[Users.Module] {
    override def apply[A](input: Users.Module[A]): Task[A] = input match {
      case Users.FindById(uid) ⇒
        Task.delay(some(User(uid, uid.repr.reverse, 23)))
    }
  }
}

trait UsersBlockingInterpreterInstance {
  implicit val usersBlockingInterpreterInstance: Blocking[Users.Module] = new Blocking[Users.Module] {
    override def apply[A](input: Users.Module[A]): Id[A] = input match {
      case Users.FindById(uid) ⇒
        some(User(uid, uid.repr.reverse, 23))
    }
  }
}
