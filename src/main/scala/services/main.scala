package services

import cats.data.Coproduct
import cats.free.Inject
import cats.{ Id, ~> }
import services.algebras.interpreter._
import services.algebras.interpreter.blocking.all._
import services.algebras.queue._
import services.algebras.stdio._
import services.algebras.timer._

object HelloWorld extends Program[Id, Unit] {

  type Fr0[A] = Coproduct[StdIOOp, TimerOp, A]
  type Frg[A] = Coproduct[QueueOp, Fr0, A]

  val Queue = QueueAlgebra(Inject[QueueOp, Frg])
  val StdIO = StdIOAlgebra(Inject[StdIOOp, Frg])
  val Timer = TimerAlgebra(Inject[TimerOp, Frg])

  override val I: Frg ~> Id = implicitly[Frg ~> Id]

  override val receipt: Prg =
    for {
      input <- StdIO.get("What's your name?")
      time0 <- Timer.get
      _ <- Queue.put(input)
      value <- Queue.pop
      time1 <- Timer.get
      _ <- StdIO.put(Seq(value, value.map(_.reverse)).flatten.toString())
      _ <- StdIO.put(s"Secs : ${(time1 - time0) / 1000.0}")
    } yield ()
}

object main extends App {

  HelloWorld.program()
}
