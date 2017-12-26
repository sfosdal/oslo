package net.fosdal.oslo.util

import net.fosdal.oslo.util.OsloGen._
import org.joda.time.DateTime
import org.scalacheck.Arbitrary

import scala.concurrent.duration.{Duration, FiniteDuration}

object OsloArbitrary {

  implicit lazy val arbDuration: Arbitrary[Duration]             = Arbitrary(genDuration)
  implicit lazy val arbFiniteDuration: Arbitrary[FiniteDuration] = Arbitrary(genFiniteDuration)
  implicit lazy val arbDateTime: Arbitrary[DateTime]             = Arbitrary(genDateTime)

}
