package services.modules

import cats.InjectK
import cats.free.Free

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

object Users {
  sealed trait Module[A]

  final case class FindById[A](userID: UserID) extends Module[Option[User]]
}

trait UsersFunctions {
  def findById[M[_]](uid: UserID)(
      implicit I: InjectK[Users.Module, M]): Free[M, Option[User]] =
    Free.inject[Users.Module, M](Users.FindById(uid))
}
