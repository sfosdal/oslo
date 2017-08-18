package net.fosdal.oslo

import org.scalacheck.Gen
import org.scalacheck.Gen.{chooseNum, const, frequency}

import scala.Long.{MaxValue, MinValue}
import scala.concurrent.duration.Duration._
import scala.concurrent.duration.{Duration, FiniteDuration}

package object util {

  implicit val genDuration: Gen[Duration] = frequency(
    1 -> const(Inf),
    1 -> const(MinusInf),
    1 -> const(Undefined),
    1 -> const(Zero),
    6 -> chooseNum(MinValue + 1, MaxValue).map(fromNanos)
  )

  implicit val genFiniteDuration: Gen[FiniteDuration] = {
    chooseNum(MinValue + 1, MaxValue).map(fromNanos)
  }

}
