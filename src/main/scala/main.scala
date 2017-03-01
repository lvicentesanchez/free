import cats._, syntax.flatMap._, syntax.functor._

import scala.io.StdIn

trait Console[M[_]] {
  def read(): M[String]
  def write(string: String): M[Unit]
}

object Console {

  def Impl[M[_]](implicit M: Monad[M]): Console[M] = new Console[M] {
    def read(): M[String] = M.pure(StdIn.readLine("Your name: "))
    def write(string: String): M[Unit] = M.pure(println(string))
  }
}

object main extends App {

  def program[M[_]](implicit M: Monad[M], C: Console[M]): M[Unit] =
    for {
      s <- C.read()
      _ <- C.write(s)
    } yield ()

  program[Id](Monad[Id], Console.Impl[Id])
}
