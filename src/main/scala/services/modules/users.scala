package services.modules

import java.io.IOException

import scalaz.zio.ZIO

case class UserID(repr: String)

case class User(uid: UserID, name: String, age: Int)

trait Users {

  val users: Users.Service[Any]

}

object Users {

  trait Service[A] {

    def findById(uid: UserID): ZIO[Any, IOException, Option[User]]

  }

  trait Live extends Users {

    override val users: Service[Any] = new Service[Any] {

      override def findById(uid: UserID): ZIO[Any, IOException, Option[User]] =
        ZIO.effect(Option(User(uid, uid.repr.reverse, 23))).refineOrDie {
          case e: IOException => e
        }

    }

  }

  object Live extends Live

}
