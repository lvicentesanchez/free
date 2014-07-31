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
  type Ex0[A] = Coproduct[QueueModule, UsersModule, A]
  type Exe[A] = Coproduct[StdIO.Module, Ex0, A]
  type Prg[A] = Free[Exe, A]

  implicit val int0 = new QueueBlocking
  implicit val int1 = new StdIOBlocking
  implicit val int2 = new UsersBlocking

  val prg: Prg[Unit] =
    for {
      input ← stdio.get[Exe]("What's your name?")
      _ ← queue.put[Exe](QueueID("myqueue"), input)
      value ← queue.get[Exe](QueueID("myqueue"))
      users ← value.fold(Free.point[Exe, Option[User]](none[User]))(v ⇒ users.findById[Exe](UserID(v)))
      _ ← stdio.put[Exe](Seq(value, users.map(_.name)).flatten.toString())
    } yield ()

  (new Interpreter[Exe, Id] {})(prg)
}
