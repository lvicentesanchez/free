package services

package object modules {
  object queue extends QueueInstances with QueueFunctions
  object users extends UsersInstances with UsersFunctions

  object all extends QueueInstances with QueueFunctions with UsersInstances with UsersFunctions
}
