package services

package object modules {
  object queue extends QueueModule
  object stdio extends StdIOModule
  object timer extends TimerFunctions
  object users extends UsersFunctions
  object value extends ValueFunctions

  /*object all
    extends QueueModule
    with StdIOModule
    with TimerFunctions
    with UsersFunctions
    with ValueFunctions*/
}
