package net.fosdal.oslo.util

import org.joda.time.DateTime
import org.scalacheck.Gen
import org.scalacheck.Gen._

import scala.concurrent.duration.{Duration, FiniteDuration}

object OsloGen {

  val genDateTime: Gen[DateTime] = chooseNum(0, Long.MaxValue).map(new DateTime(_))

  // ScalaTest Issue: https://github.com/scalatest/scalatest/issues/1251
  val genFiniteDuration: Gen[FiniteDuration] = chooseNum(Long.MinValue, Long.MaxValue).map(Duration.fromNanos)

  val genDuration: Gen[Duration] = {
    frequency(
      1  -> const(Duration.Inf),
      1  -> const(Duration.MinusInf),
      1  -> const(Duration.Undefined),
      1  -> const(Duration.Zero),
      10 -> genFiniteDuration
    )
  }

}
