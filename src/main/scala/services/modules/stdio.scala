package services.modules

import scalaz.{ Free, Functor, Inject, InjectFunctions }

object StdIO {
  sealed trait Module[A]
  final case class Get[A](prompt: String, f: String ⇒ A) extends Module[A]
  final case class Put[A](output: String, f: Unit ⇒ A) extends Module[A]
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
    Free.liftF(I.inj(StdIO.Get(prompt, identity)))

  def put[F[_]: Functor](output: String)(implicit I: Inject[StdIO.Module, F]): Free[F, Unit] =
    Free.liftF(I.inj(StdIO.Put(output, identity)))
}