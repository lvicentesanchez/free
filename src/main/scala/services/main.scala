package services

import cats.Id
import cats.data.Coproduct
import cats.free.{ Free, Inject }
import services.modules.interpreter._
import services.modules.interpreter.blocking.all._
import services.modules.queue._
import services.modules.stdio._
import services.modules.timer._

object main extends App {
  type Fr0[A] = Coproduct[StdIOOp, TimerOp, A]
  type Frg[A] = Coproduct[QueueOp, Fr0, A]
  type Prg[A] = Free[Frg, A]

  val Q = new QueueModule[Frg](Inject[QueueOp, Frg])
  val S = new StdIOModule[Frg](Inject[StdIOOp, Frg])
  val T = new TimerModule[Frg](Inject[TimerOp, Frg])

  val program: Prg[Unit] =
    for {
      input ← S.get("What's your name?")
      time0 ← T.get
      _ ← Q.put(input)
      valuu ← Q.pop
      time1 ← T.get
      _ ← S.put(Seq(valuu, valuu.map(_.reverse)).flatten.toString())
      _ ← S.put(s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  program.runI[Id]
}
