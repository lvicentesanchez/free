package services.modules.interpreter

import cats.Id
import services.modules.timer._

trait TimerBlockingInterpreterInstance {
  implicit val timerBlockingInterpreterIntance: Blocking[TimerOp] = new TimerBlockingInterpreter {}
}

trait TimerBlockingInterpreter extends Blocking[TimerOp] {
  override def apply[A](input: TimerOp[A]): Id[A] = input match {
    case Get ⇒ System.currentTimeMillis()
  }
}

object TimerBlockingInterpreter extends TimerBlockingInterpreter
