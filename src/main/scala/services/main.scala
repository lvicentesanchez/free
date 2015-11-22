package services

import services.modules._
import services.modules.interpreter._
import services.modules.interpreter.blocking.all._

import scalaz.Id._
import scalaz.std.option._
import scalaz.{ Coproduct, Free }

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
      valuu ← queue.get[Frg](QueueID("myqueue"))
      users ← valuu.fold(value.pure[Frg, Option[User]](none[User]))(v ⇒ users.findById[Frg](UserID(v)))
      time1 ← timer.get[Frg]()
      _ ← stdio.put[Frg](Seq(valuu, users.map(_.name)).flatten.toString())
      _ ← stdio.put[Frg](s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  program.runI[Id]
}
