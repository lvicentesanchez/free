package services.modules.interpreter

import scalaz.Id._
import scalaz.concurrent.Task
import services.modules.Timer

trait TimerAsyncInterpreterInstance {
  implicit val timerAsyncInterpreterIntance: Asynchronous[Timer.Module] = new Asynchronous[Timer.Module] {
    override def apply[A](input: Timer.Module[A]): Task[A] = input match {
      case Timer.Get ⇒
        Task.delay(System.currentTimeMillis())
    }
  }
}

trait TimerBlockingInterpreterInstance {
  implicit val timerBlockingInterpreterIntance: Blocking[Timer.Module] = new Blocking[Timer.Module] {
    override def apply[A](input: Timer.Module[A]): Id[A] = input match {
      case Timer.Get ⇒
        System.currentTimeMillis()
    }
  }
}
