package services.modules.interpreter

import cats.Id
import services.modules.Timer

trait TimerBlockingInterpreterInstance {
  implicit val timerBlockingInterpreterIntance: Blocking[Timer.Module] = new Blocking[Timer.Module] {
    override def apply[A](input: Timer.Module[A]): Id[A] = input match {
      case Timer.Get ⇒
        System.currentTimeMillis()
    }
  }
}
