package net.fosdal.oslo.util

import net.fosdal.oslo.util.OsloGen._
import org.joda.time.DateTime
import org.scalacheck.Arbitrary

import scala.concurrent.duration.{Duration, FiniteDuration}

object OsloArbitrary {

  implicit lazy val arbNegativeInt: Arbitrary[Int] = Arbitrary(genNegativeInt)
  implicit lazy val arbPositiveInt: Arbitrary[Int] = Arbitrary(genPositiveInt)
  implicit lazy val arbNonZeroInt: Arbitrary[Int]  = Arbitrary(genNonZeroInt)

  implicit lazy val arbNegativeLong: Arbitrary[Long] = Arbitrary(genNegativeLong)
  implicit lazy val arbPositiveLong: Arbitrary[Long] = Arbitrary(genPositiveLong)
  implicit lazy val arbNonZeroLong: Arbitrary[Long]  = Arbitrary(genNonZeroLong)

  implicit lazy val arbDuration: Arbitrary[Duration]             = Arbitrary(genDuration)
  implicit lazy val arbFiniteDuration: Arbitrary[FiniteDuration] = Arbitrary(genFiniteDuration)
  implicit lazy val arbDateTime: Arbitrary[DateTime]             = Arbitrary(genDateTime)

}
