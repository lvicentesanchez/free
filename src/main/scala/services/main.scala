package services

import java.util.concurrent.TimeUnit

import scalaz.zio._
import scalaz.zio.clock.Clock
import scalaz.zio.console._
import scalaz.zio.internal.PlatformLive
import services.modules._

object Env
  extends Clock.Live
  with Console.Live
  with Users.Live

object main {

  def main(args: Array[String]): Unit = {

    val runtime = Runtime(Env, PlatformLive.Default)

    val program =
      for {
        _ <- console.putStrLn("What's your name? ")
        input <- console.getStrLn
        time0 <- clock.currentTime(TimeUnit.MILLISECONDS)
        users <- users.findById(UserID(input))
        time1 <- clock.currentTime(TimeUnit.MILLISECONDS)
        _ <- console.putStrLn(Seq(input, users).mkString(" -> "))
        _ <- console.putStrLn(s"Secs : ${(time1 - time0) / 1000.0}")
      } yield ()

    runtime.unsafeRun(program)
  }
}
