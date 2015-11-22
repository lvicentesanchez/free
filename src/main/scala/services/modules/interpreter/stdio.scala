package services.modules.interpreter

import cats.Id
import services.modules.StdIO

import scala.io.StdIn

trait StdIOBlockingInterpreterInstance {
  implicit val stdioBlockingInterpreterIntance: Blocking[StdIO.Module] = new Blocking[StdIO.Module] {
    override def apply[A](input: StdIO.Module[A]): Id[A] = input match {
      case StdIO.Put(output) ⇒
        println(output)

      case StdIO.Get(prompt) ⇒
        println(prompt)
        StdIn.readLine()
    }
  }
}

