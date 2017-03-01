package services

package object modules {
  object queue extends QueueFunctions
  object stdio extends StdIOFunctions
  object timer extends TimerFunctions
  object users extends UsersFunctions
  object value extends ValueFunctions

  object all
      extends QueueFunctions
      with StdIOFunctions
      with TimerFunctions
      with UsersFunctions
      with ValueFunctions
}
