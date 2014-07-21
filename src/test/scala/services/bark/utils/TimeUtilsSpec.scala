package services.bark.utils

import org.specs2._
import org.scalacheck.Gen
import org.scalacheck.Prop._

class TimeUtilsSpec extends Specification with ScalaCheck {
  def is = s2"""

 Testing services.bark.utils.TimeUtils

 The roundMinutes method should
   return a number that has a remainder of 0 after dividing it by the minute interval $specRoundMinutesRemainder
                                                                 """

  def specRoundMinutesRemainder = forAll(Gen.posNum[Int], Gen.posNum[Int]) { (time: Int, mins: Int) ⇒
    TimeUtils.roundMinutes(time, mins) % (mins * 60) must_== 0
  }
}