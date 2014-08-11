package services

package object modules {
  object queue extends QueueInstances with QueueFunctions
  object stdio extends StdIOFunctions
  object timer extends TimerFunctions
  object users extends UsersInstances with UsersFunctions

  object all
    extends QueueInstances
    with QueueFunctions
    with StdIOFunctions
    with TimerFunctions
    with UsersInstances
    with UsersFunctions
}
