package services.modules

import scalaz.{ Free ⇒ F, Inject, InjectFunctions }

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

object Users {
  sealed trait Module[A]

  final case class FindById[A](userID: UserID) extends Module[Option[User]]
}

trait UsersFunctions extends InjectFunctions {
  def findById[M[_]](uid: UserID)(implicit I: Inject[Users.Module, M]): F.FreeC[M, Option[User]] =
    F.liftFC(I.inj(Users.FindById(uid)))
}