package services

import scalaz.Id._
import scalaz.{ Monad, Coproduct, Free }
import scalaz.std.option.{ optionFirstMonad ⇒ _, optionLastMonad ⇒ _, optionMaxMonad ⇒ _, optionMinMonad ⇒ _, _ }
import scalaz.syntax.std.option._
import services.modules._
import services.modules.all._
import services.modules.interpreter._
import services.modules.interpreter.Blocking._

object main extends App {
  type Exe[A] = Coproduct[QueueModule, UsersModule, A]
  type Prg[A] = Free[Exe, A]

  implicit val int1 = new QueueBlocking
  implicit val int2 = new UsersBlocking

  val prg: Prg[Seq[String]] =
    for {
      _ ← queue.put[Exe](QueueID("myqueue"), "Elephant")
      _ ← queue.put[Exe](QueueID("myqueue"), "Donkey")
      value ← queue.get[Exe](QueueID("myqueue"))
      users ← value.fold(Free.point[Exe, Option[User]](none[User]))(v ⇒ users.findById[Exe](UserID(v)))
    } yield Seq(value, users.map(_.name)).flatten

  println((new Interpreter[Exe, Id] {})(prg))
}
