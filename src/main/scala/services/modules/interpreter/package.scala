package services.modules

package object interpreter {
  object blocking {
    object queue extends QueueBlockingInterpreterInstance
    object stdio extends StdIOBlockingInterpreterInstance
    object timer extends TimerBlockingInterpreterInstance
    object users extends UsersBlockingInterpreterInstance

    object all extends QueueBlockingInterpreterInstance with StdIOBlockingInterpreterInstance with TimerBlockingInterpreterInstance with UsersBlockingInterpreterInstance
  }
}
