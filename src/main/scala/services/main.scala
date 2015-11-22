/*package services

import cats.Id
import cats.data.Coproduct
import cats.free.Free
import services.modules._
import services.modules.interpreter._
import services.modules.interpreter.blocking.all._

object main extends App {
  type Fr2[A] = Users.Module[A]
  type Fr1[A] = Coproduct[Timer.Module, Fr2, A]
  type Fr0[A] = Coproduct[queue.QueueOperations, Fr1, A]
  type Frg[A] = Coproduct[StdIO.Module, Fr0, A]
  type Prg[A] = Free[Frg, A]

  val program: Prg[Unit] =
    for {
      input ← stdio.get[Frg]("What's your name?")
      time0 ← timer.get[Frg]()
      _ ← queue.put[Frg](QueueID("myqueue"), input)
      valuu ← queue.get[Frg](QueueID("myqueue"))
      users ← valuu match {
        case Some(v) ⇒ users.findById[Frg](UserID(v))
        case None ⇒ value.pure[Frg, Option[User]](None)
      }
      time1 ← timer.get[Frg]()
      _ ← stdio.put[Frg](Seq(valuu, users.map(_.name)).flatten.toString())
      _ ← stdio.put[Frg](s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  program.runI[Id]
}
*/ 