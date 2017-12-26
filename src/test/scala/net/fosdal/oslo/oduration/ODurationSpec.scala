package net.fosdal.oslo.oduration

import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._

// scalastyle:off magic.number
class ODurationSpec extends WordSpec with Matchers with PropertyChecks {

  private[this] val prettyPrecision = 1

  "asDuration" must {

    "convert a org.joda.time.Duration to a scala.concurrent.duration.Duration" in {
      forAll() { n: Long =>
        val millis = n / 1000000
        val d = new org.joda.time.Duration(millis)
        asDuration(d) shouldBe millis.milliseconds
      }
    }

    "convert a org.joda.time.Interval to a scala.concurrent.duration.Duration" in {
      asDuration(new org.joda.time.Interval(0, 0)) shouldBe 0.milliseconds
      asDuration(new org.joda.time.Interval(0, 1)) shouldBe 1.milliseconds
      asDuration(new org.joda.time.Interval(0, 1000)) shouldBe 1000.milliseconds
      asDuration(new org.joda.time.Interval(0, 2356257)) shouldBe 2356257.milliseconds
    }

  }

  "abs" must {
    "return the absolute value of the duration" in {
      2.days.abs shouldBe 2.days
      -2.days.abs shouldBe 2.days
      Zero.abs shouldBe 0.days
      Inf.abs shouldBe Inf
      MinusInf.abs shouldBe Inf
      Undefined.abs shouldBe Undefined
    }
    "support the static method form" in {
      abs(2.days) shouldBe 2.days
      abs(-2.days) shouldBe 2.days
      abs(Zero) shouldBe 0.days
      abs(Inf) shouldBe Inf
      abs(MinusInf) shouldBe Inf
      abs(Undefined) shouldBe Undefined
    }
  }

  "pretty" must {
    "support the static method form" in {
      pretty((-200).nanoseconds) shouldBe "-200.0ns"
      pretty(2.nanoseconds) shouldBe "2.0ns"
      pretty(999.nanoseconds) shouldBe "999.0ns"
      pretty(1001.nanoseconds) shouldBe "1.0µs"
      pretty(1050.nanoseconds) shouldBe "1.1µs"
    }
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
      Zero.pretty shouldBe "0.0ms"
      Inf.pretty shouldBe "Infinity"
      MinusInf.pretty shouldBe "-Infinity"
      Undefined.pretty shouldBe "Undefined"
    }
  }

  "pretty with precision" must {
    "support the static method form" in {
      pretty(2.milliseconds, 2) shouldBe "2.00ms"
      pretty(999.milliseconds, 2) shouldBe "999.00ms"
      pretty(1001.milliseconds, 2) shouldBe "1.00s"
      pretty(1055.milliseconds, 2) shouldBe "1.06s"
    }
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
      forAll(Gen.chooseNum(Long.MinValue + 1L, Long.MaxValue)) { nanos =>
        nanos.nanoseconds.pretty(prettyPrecision) shouldBe nanos.nanoseconds.pretty
        pretty(nanos.nanoseconds, prettyPrecision) shouldBe nanos.nanoseconds.pretty
      }
    }
  }

  "asFiniteDuration" must {

    "convert finite durations to FiniteDurations" in {
      forAll(Gen.chooseNum(Long.MinValue + 1L, Long.MaxValue)) { nanos: Long =>
        val d: Duration = nanos.nanoseconds
        val fd: FiniteDuration = asFiniteDuration(d)
        fd shouldBe nanos.nanoseconds
      }
    }

    "throw exceptions when used on a non-finite duration" in {
      an[IllegalArgumentException] should be thrownBy {
        asFiniteDuration(Duration.Inf)
      }
      an[IllegalArgumentException] should be thrownBy {
        asFiniteDuration(Duration.MinusInf)
      }
      an[IllegalArgumentException] should be thrownBy {
        asFiniteDuration(Duration.Undefined)
      }
    }

  }

  "toFiniteDuration" must {

    "convert finite durations to Option[FiniteDuration]s" in {
      forAll(Gen.chooseNum(Long.MinValue + 1L, Long.MaxValue)) { nanos: Long =>
        val d: Duration = nanos.nanoseconds
        val fd: Option[FiniteDuration] = d.toFiniteDuration
        fd shouldBe Some(nanos.nanoseconds)
      }
    }

    "return Nones used on a non-finite duration" in {
      Duration.Inf.toFiniteDuration shouldBe None
      Duration.MinusInf.toFiniteDuration shouldBe None
      Duration.Undefined.toFiniteDuration shouldBe None
    }

  }

}
