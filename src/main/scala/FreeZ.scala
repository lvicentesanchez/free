import scala.io.StdIn
import scalaz.{ ~>, Id, Free, Functor }, Free.Return, Free.Suspend, Id.Id
import scalaz.syntax.std.boolean._
import scalaz.syntax.std.string._

sealed trait IO[+A]

object IO {
  type ConsoleF[A] = Free[IO, A]

  case class Ask[A](promt: String, o: String ⇒ A) extends IO[A]
  case class Tell[A](message: String, o: A) extends IO[A]

  implicit val stdIOFunctor: Functor[IO] = new Functor[IO] {
    override def map[A, B](fa: IO[A])(f: A ⇒ B): IO[B] = fa match {
      case Ask(prompt, g) ⇒ Ask(prompt, s ⇒ f(g(s)))
      case Tell(message, a) ⇒ Tell(message, f(a))
    }
  }

  implicit def liftF[F[+_]: Functor, A](f: F[A]): Free[F, A] =
    Suspend[F, A](Functor[F].map(f)(Return[F, A](_)))

  object io {
    def ask(prompt: String): ConsoleF[String] = Ask(prompt, identity)
    def tell(message: String): ConsoleF[Unit] = Tell(message, ())
  }
}

object ConsoleIO {
  val exe: IO ~> Id = new (IO ~> Id) {
    def apply[A](io: IO[A]): Id[A] = io match {
      case IO.Ask(prompt, f) ⇒
        println(prompt)
        f(StdIn.readLine())

      case IO.Tell(message, value) ⇒
        println(message)
        value
    }
  }

  def apply[A](io: IO.ConsoleF[A]): A =
    io.runM(exe.apply[IO.ConsoleF[A]])
}

object FreeZ extends App {
  import IO._, io._

  val app: ConsoleF[Unit] = for {
    usr ← ask("What is your name?")
    age ← ask("What is your age?")
    _ ← tell(s"Hello ${usr.toUpperCase}!")
    _ ← age.parseInt.map(n ⇒ (n > 18) ? tell("You are old!") | tell("You aren't old enough")) | tell("That's not even a number!")
  } yield ()

  ConsoleIO(app)
}

