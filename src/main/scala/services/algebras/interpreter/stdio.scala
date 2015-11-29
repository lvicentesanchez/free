package services.algebras.interpreter

import cats.Id
import services.algebras.stdio._

import scala.io.StdIn

trait StdIOBlockingInterpreterInstance {
  implicit val stdioBlockingInterpreterIntance: Blocking[StdIOOp] = new StdIOBlockingInterpreter {}
}

trait StdIOBlockingInterpreter extends Blocking[StdIOOp] {
  override def apply[A](input: StdIOOp[A]): Id[A] = input match {
    case Put(output) => println(output)

    case Get(prompt) => println(prompt); StdIn.readLine()
  }
}

object StdIOBlockingInterpreter extends StdIOBlockingInterpreter
