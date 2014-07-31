package services.modules.interpreter

import scalaz.Id._
import scalaz.{ ~>, -\/, \/-, Coproduct }

trait Blocking[F[_]] extends (F ~> Id)

trait CoproductL[F[_], G[_]] {
  type M[A] = Coproduct[F, G, A]
}

object Blocking {
  implicit def coproductBlockingInterpreter[F[_]: Blocking, G[_]: Blocking] = new Blocking[CoproductL[F, G]#M] {
    def apply[A](input: CoproductL[F, G]#M[A]) = input.run match {
      case -\/(fa) ⇒ implicitly[Blocking[F]].apply(fa)
      case \/-(ga) ⇒ implicitly[Blocking[G]].apply(ga)
    }
  }
}