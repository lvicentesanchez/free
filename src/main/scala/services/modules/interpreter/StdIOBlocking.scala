package services.modules.interpreter

import scala.io.StdIn
import scalaz.Id._
import services.modules.StdIO

class StdIOBlocking extends Blocking[StdIO.Module] {
  override def apply[A](input: StdIO.Module[A]): Id[A] = input match {
    case StdIO.Put(output, f) ⇒
      f(println(output))

    case StdIO.Get(prompt, f) ⇒
      println(prompt)
      f(StdIn.readLine())
  }
}
