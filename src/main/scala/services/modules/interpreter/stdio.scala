package services.modules.interpreter

import scala.io.StdIn
import scalaz.Id._
import scalaz.concurrent.Task
import services.modules.StdIO

trait StdIOAsyncInterpreterInstance {
  implicit val stdioAsyncInterpreterIntance: Asynchronous[StdIO.Module] = new Asynchronous[StdIO.Module] {
    override def apply[A](input: StdIO.Module[A]): Task[A] = input match {
      case StdIO.Put(output, f) ⇒
        Task.delay(f(println(output)))

      case StdIO.Get(prompt, f) ⇒
        for {
          _ ← Task.delay(println(prompt))
          i ← Task.delay(f(StdIn.readLine()))
        } yield i
    }
  }
}

trait StdIOBlockingInterpreterInstance {
  implicit val stdioBlockingInterpreterIntance: Blocking[StdIO.Module] = new Blocking[StdIO.Module] {
    override def apply[A](input: StdIO.Module[A]): Id[A] = input match {
      case StdIO.Put(output, f) ⇒
        f(println(output))

      case StdIO.Get(prompt, f) ⇒
        println(prompt)
        f(StdIn.readLine())
    }
  }
}

