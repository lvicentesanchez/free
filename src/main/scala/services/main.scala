package services

import scalaz.Id._
import scalaz.{ Coproduct, Free }
import scalaz.concurrent.Task
import scalaz.std.option._
import services.modules._
import services.modules.all._
import services.modules.interpreter._
import services.modules.interpreter.blocking.all._

object main extends App {
  type Fr2[A] = Users.Module[A]
  type Fr1[A] = Coproduct[Timer.Module, Fr2, A]
  type Fr0[A] = Coproduct[Queue.Module, Fr1, A]
  type Frg[A] = Coproduct[StdIO.Module, Fr0, A]
  type Prg[A] = Free[Frg, A]

  val program: Prg[Unit] =
    for {
      input ← stdio.get[Frg]("What's your name?")
      time0 ← timer.get[Frg]()
      _ ← queue.put[Frg](QueueID("myqueue"), input)
      value ← queue.get[Frg](QueueID("myqueue"))
      users ← value.fold(Free.point[Frg, Option[User]](none[User]))(v ⇒ users.findById[Frg](UserID(v)))
      time1 ← timer.get[Frg]()
      _ ← stdio.put[Frg](Seq(value, users.map(_.name)).flatten.toString())
      _ ← stdio.put[Frg](s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  val produce: Prg[Long] =
    for {
      input ← stdio.get[Frg]("What's your name?")
      time0 ← timer.get[Frg]()
      _ ← queue.put[Frg](QueueID("myqueue"), input)
    } yield time0

  val consume: Prg[Long] =
    for {
      value ← queue.get[Frg](QueueID("myqueue"))
      users ← value.fold(Free.point[Frg, Option[User]](none[User]))(v ⇒ users.findById[Frg](UserID(v)))
      time1 ← timer.get[Frg]()
      _ ← stdio.put[Frg](Seq(value, users.map(_.name)).flatten.toString())
    } yield time1

  val compose: Prg[Unit] =
    for {
      time0 ← produce
      time1 ← consume
      _ ← stdio.put[Frg](s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  Interpreter[Frg, Id](program)
}
