package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

sealed trait UsersModule[A]

final case class FindById[A](userID: UserID, f: (⇒ Option[User]) ⇒ A) extends UsersModule[A]

trait UsersInstances {
  implicit val usersAlgebraFunctor: Functor[UsersModule] = new Functor[UsersModule] {
    override def map[A, B](a: UsersModule[A])(f: A ⇒ B) = a match {
      case FindById(uid, g) ⇒ FindById(uid, a ⇒ f(g(a)))
    }
  }
}

trait UsersFunctions extends InjectFunctions {
  def findById[F[_]: Functor](uid: UserID)(implicit I: Inject[UsersModule, F]): Free[F, Option[User]] =
    inject[F, UsersModule, Option[User]](FindById(uid, Free.point))
}