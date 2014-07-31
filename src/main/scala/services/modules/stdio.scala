package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

object StdIO {
  sealed trait Module[A]
  final case class Get[A](prompt: String, f: (⇒ String) ⇒ A) extends Module[A]
  final case class Put[A](output: String, f: (⇒ Unit) ⇒ A) extends Module[A]
}

trait StdIOInstances {
  implicit val stdioAlgebraFunctor: Functor[StdIO.Module] = new Functor[StdIO.Module] {
    override def map[A, B](a: StdIO.Module[A])(f: A ⇒ B) = a match {
      case StdIO.Get(prompt, g) ⇒ StdIO.Get(prompt, a ⇒ f(g(a)))
      case StdIO.Put(output, g) ⇒ StdIO.Put(output, a ⇒ f(g(a)))
    }
  }
}

trait StdIOFunctions extends InjectFunctions {
  def get[F[_]: Functor](prompt: String)(implicit I: Inject[StdIO.Module, F]): Free[F, String] =
    inject[F, StdIO.Module, String](StdIO.Get(prompt, Free.point))

  def put[F[_]: Functor](output: String)(implicit I: Inject[StdIO.Module, F]): Free[F, Unit] =
    inject[F, StdIO.Module, Unit](StdIO.Put(output, Free.point))
}