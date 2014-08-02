package services

import services.modules.{ User, UserID }

import scala.collection.mutable
import scala.io.StdIn
import scalaz.std.option._

object base extends App {
  val mem: mutable.Queue[String] = new mutable.Queue[String]()

  println("What's your name?")
  val name: String = StdIn.readLine()
  val time0: Long = System.currentTimeMillis()
  mem += name
  val value: Option[String] = mem.dequeueFirst(_ ⇒ true)
  val users: Option[User] = value.fold(none[User])(v ⇒ some(User(UserID(v), UserID(v).repr.reverse, 23)))
  val time1: Long = System.currentTimeMillis()
  println(Seq(value, users.map(_.name)).flatten.toString())
  println(s"Secs : ${(time1 - time0) / 1000.0}")
}
