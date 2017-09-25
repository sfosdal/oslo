package net.fosdal.oslo.oduration

import net.fosdal.oslo.util._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._

// scalastyle:off magic.number
class ODurationSpec extends WordSpec with Matchers with PropertyChecks {

  val DefaultPrettyPrecision = 1

  "abs" must {
    "return the absolute value of the duration" in {
      2.days.abs shouldBe 2.days
      -2.days.abs shouldBe 2.days
      Zero.abs shouldBe 0.days
      Inf.abs shouldBe Inf
      MinusInf.abs shouldBe Inf
      Undefined.abs shouldBe Undefined
    }
  }

  "pretty" must {
    "format some typical nanoseconds" in {
      (-200).nanoseconds.pretty shouldBe "-200.0ns"
      2.nanoseconds.pretty shouldBe "2.0ns"
      999.nanoseconds.pretty shouldBe "999.0ns"
      1001.nanoseconds.pretty shouldBe "1.0µs"
      1050.nanoseconds.pretty shouldBe "1.1µs"
    }
    "format some typical milliseconds" in {
      (-200).milliseconds.pretty shouldBe "-200.0ms"
      2.milliseconds.pretty shouldBe "2.0ms"
      999.milliseconds.pretty shouldBe "999.0ms"
      1001.milliseconds.pretty shouldBe "1.0s"
      1050.milliseconds.pretty shouldBe "1.1s"
    }
    "format some typical seconds" in {
      (-200).seconds.pretty shouldBe "-3.3m"
      2.seconds.pretty shouldBe "2.0s"
      61.seconds.pretty shouldBe "1.0m"
      63.seconds.pretty shouldBe "1.1m"
    }
    "format some typical minutes" in {
      (-200).minutes.pretty shouldBe "-3.3h"
      2.minutes.pretty shouldBe "2.0m"
      61.minutes.pretty shouldBe "1.0h"
      63.minutes.pretty shouldBe "1.1h"
    }
    "format some typical hours" in {
      (-200).hours.pretty shouldBe "-8.3d"
      2.hours.pretty shouldBe "2.0h"
      25.hours.pretty shouldBe "1.0d"
      26.hours.pretty shouldBe "1.1d"
    }
    "format some typical days" in {
      (-200).days.pretty shouldBe "-200.0d"
      2.days.pretty shouldBe "2.0d"
      25.days.pretty shouldBe "25.0d"
      56.days.pretty shouldBe "56.0d"
    }
    "format some special durations" in {
      Zero.pretty shouldBe "0ms"
      Inf.pretty shouldBe "Infinity"
      MinusInf.pretty shouldBe "-Infinity"
      Undefined.pretty shouldBe "Undefined"
    }
  }

  "pretty with precision" must {
    "format some typical milliseconds" in {
      2.milliseconds.pretty(2) shouldBe "2.00ms"
      999.milliseconds.pretty(2) shouldBe "999.00ms"
      1001.milliseconds.pretty(2) shouldBe "1.00s"
      1055.milliseconds.pretty(2) shouldBe "1.06s"
    }
    "format some typical seconds" in {
      2.seconds.pretty(0) shouldBe "2s"
      61.seconds.pretty(0) shouldBe "1m"
      63.seconds.pretty(0) shouldBe "1m"
      119.seconds.pretty(0) shouldBe "2m"
    }
    "pretty with precision 1 must be same as default" in {
      forAll(genDuration) { d =>
        d.pretty(DefaultPrettyPrecision) shouldBe d.pretty
      }
    }
  }

  "toFiniteDuration" must {

    "convert finite durations to FiniteDurations" in {
      val d: Duration = 5.seconds
      val fd: FiniteDuration = d.toFiniteDuration
      fd.toSeconds shouldBe 5
      Duration.Zero.toFiniteDuration
      someMethod(fd)

      def someMethod(x: FiniteDuration): Unit = ()
    }

    "throw exceptions when used on a non-finite duration" in {
      an[IllegalArgumentException] should be thrownBy {
        Duration.Inf.toFiniteDuration
      }
      an[IllegalArgumentException] should be thrownBy {
        Duration.MinusInf.toFiniteDuration
      }
      an[IllegalArgumentException] should be thrownBy {
        Duration.Undefined.toFiniteDuration
      }
    }

  }

}
