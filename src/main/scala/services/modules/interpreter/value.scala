package services.modules.interpreter

import scalaz.Id._
import scalaz.concurrent.Task
import services.modules.Value

trait ValueAsyncInterpreterInstance {
  implicit val ValueAsyncInterpreterIntance: Asynchronous[Value.Module] = new Asynchronous[Value.Module] {
    override def apply[A](input: Value.Module[A]): Task[A] = input match {
      case Value.Pure(a, f) ⇒
        Task.delay(f(a))
    }
  }
}

trait ValueBlockingInterpreterInstance {
  implicit val ValueBlockingInterpreterIntance: Blocking[Value.Module] = new Blocking[Value.Module] {
    override def apply[A](input: Value.Module[A]): Id[A] = input match {
      case Value.Pure(a, f) ⇒
        f(a)
    }
  }
}
