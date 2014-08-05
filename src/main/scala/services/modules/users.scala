package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

object Users {
  sealed trait Module[A]

  final case class FindById[A](userID: UserID, f: Option[User] ⇒ A) extends Module[A]
}

trait UsersInstances {
  implicit val usersAlgebraFunctor: Functor[Users.Module] = new Functor[Users.Module] {
    override def map[A, B](a: Users.Module[A])(f: A ⇒ B) = a match {
      case Users.FindById(uid, g) ⇒ Users.FindById(uid, a ⇒ f(g(a)))
    }
  }
}

trait UsersFunctions extends InjectFunctions {
  def findById[F[_]: Functor](uid: UserID)(implicit I: Inject[Users.Module, F]): Free[F, Option[User]] =
    Free.liftF(I.inj(Users.FindById(uid, identity)))
}