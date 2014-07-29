package services

package object modules {
  object queue extends QueueInstances with QueueFunctions
  object users extends UsersInstances with UsersFunctions
}
