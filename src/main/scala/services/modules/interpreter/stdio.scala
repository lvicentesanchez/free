package services.modules.interpreter

import cats.Id
import services.modules.stdio._

import scala.io.StdIn

trait StdIOBlockingInterpreterInstance {
  implicit val stdioBlockingInterpreterIntance: Blocking[StdIOOperations] = new StdIOBlockingInterpreter {}
}

trait StdIOBlockingInterpreter extends Blocking[StdIOOperations] {
  override def apply[A](input: StdIOOperations[A]): Id[A] = input match {
    case Put(output) ⇒
      println(output)

    case Get(prompt) ⇒
      println(prompt)
      StdIn.readLine()
  }
}

object StdIOBlockingInterpreter extends StdIOBlockingInterpreter
