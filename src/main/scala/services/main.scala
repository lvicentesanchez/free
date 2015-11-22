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

  val Queue = QueueModule(Inject[QueueOp, Frg])
  val StdIO = StdIOModule(Inject[StdIOOp, Frg])
  val Timer = TimerModule(Inject[TimerOp, Frg])

  val program: Prg[Unit] =
    for {
      input <- StdIO.get("What's your name?")
      time0 <- Timer.get
      _ <- Queue.put(input)
      value <- Queue.pop
      time1 <- Timer.get
      _ <- StdIO.put(Seq(value, value.map(_.reverse)).flatten.toString())
      _ <- StdIO.put(s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()

  program.runI[Id]
}
