package services.modules

import scalaz.{ Free, Inject, InjectFunctions }

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

object Users {
  sealed trait Module[A]

  final case class FindById[A](userID: UserID, f: Option[User] ⇒ A) extends Module[A]
}

trait UsersFunctions extends InjectFunctions {
  def findById[F[_]](uid: UserID)(implicit I: Inject[Users.Module, F]): Free.FreeC[F, Option[User]] =
    Free.liftFC(I.inj(Users.FindById(uid, identity)))
}