package services

import java.io.IOException

import scalaz.zio.ZIO

package object modules {

  object users {

    def findById(uid: UserID): ZIO[Users, IOException, Option[User]] =
      ZIO.accessM(_.users.findById(uid))

  }

}
