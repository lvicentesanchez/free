package services.modules.interpreter

import scalaz.Id._
import services.modules.Timer

class TimerBlocking extends Blocking[Timer.Module] {
  override def apply[A](input: Timer.Module[A]): Id[A] = input match {
    case Timer.Get(f) ⇒
      f(System.currentTimeMillis())
  }
}
