import cats._, instances.option._, syntax.flatMap._, syntax.functor._

import scala.io.StdIn

abstract class ConsoleRead[M[_]](implicit val M: Monad[M]) {

  def read(): M[String]
}

abstract class ConsoleWrite[M[_]](implicit val M: Monad[M]) {

  def write(string: String): M[Unit]
}

object ConsoleRead {

  final class Impl[M[_] : Monad]() extends ConsoleRead[M] {

    override def read(): M[String] = M.pure(StdIn.readLine("Your name: "))
  }

  object Impl {

    def apply[M[_] : Monad](): ConsoleRead[M] = new Impl[M]()
  }
}

object ConsoleWrite {

  final class Impl[M[_] : Monad]() extends ConsoleWrite[M] {

    override def write(string: String): M[Unit] = M.pure(println(s"Hello $string!"))
  }

  object Impl {

    def apply[M[_] : Monad](): ConsoleWrite[M] = new Impl[M]()
  }
}

object main extends App {

  def program[M[_]](cr: ConsoleRead[M], cw: ConsoleWrite[M]): M[Unit] = {
    import cr.M
    for {
      s <- cr.read()
      _ <- cw.write(s)
    } yield ()
  }

  println(program[Option](ConsoleRead.Impl(), ConsoleWrite.Impl()))
}
