package services

package object modules {
  object queue extends QueueInstances with QueueFunctions
  object stdio extends StdIOInstances with StdIOFunctions
  object users extends UsersInstances with UsersFunctions

  object all extends QueueInstances with QueueFunctions with StdIOInstances with StdIOFunctions with UsersInstances with UsersFunctions
}
