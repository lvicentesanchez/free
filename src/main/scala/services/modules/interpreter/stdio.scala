package services.modules.interpreter

import services.modules.StdIO

import scala.io.StdIn
import scalaz.Id._
import scalaz.concurrent.Task

trait StdIOAsyncInterpreterInstance {
  implicit val stdioAsyncInterpreterIntance: Asynchronous[StdIO.Module] = new Asynchronous[StdIO.Module] {
    override def apply[A](input: StdIO.Module[A]): Task[A] = input match {
      case StdIO.Put(output) ⇒
        Task.delay(println(output))

      case StdIO.Get(prompt) ⇒
        for {
          _ ← Task.delay(println(prompt))
          i ← Task.delay(StdIn.readLine())
        } yield i
    }
  }
}

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
