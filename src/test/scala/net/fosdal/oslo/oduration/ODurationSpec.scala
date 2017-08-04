package net.fosdal.oslo.oduration

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._

class ODurationSpec extends WordSpec with Matchers {

  "pretty" must {
    "format some typical milliseconds" in new Fixture {
      2.milliseconds.pretty    shouldBe "2.0ms"
      999.milliseconds.pretty  shouldBe "999.0ms"
      1001.milliseconds.pretty shouldBe "1.0s"
      1050.milliseconds.pretty shouldBe "1.1s"
    }
    "format some typical seconds" in new Fixture {
      2.seconds.pretty  shouldBe "2.0s"
      61.seconds.pretty shouldBe "1.0m"
      63.seconds.pretty shouldBe "1.1m"
    }
    "format some typical minutes" in new Fixture {
      2.minutes.pretty  shouldBe "2.0m"
      61.minutes.pretty shouldBe "1.0h"
      63.minutes.pretty shouldBe "1.1h"
    }
    "format some typical hours" in new Fixture {
      2.hours.pretty  shouldBe "2.0h"
      25.hours.pretty shouldBe "1.0d"
      26.hours.pretty shouldBe "1.1d"
    }
    "format some typical days" in new Fixture {
      2.days.pretty  shouldBe "2.0d"
      25.days.pretty shouldBe "25.0d"
      56.days.pretty shouldBe "56.0d"
    }
    "format some special durations" in new Fixture {
      Zero.pretty      shouldBe "0ms"
      Inf.pretty       shouldBe "Infinity"
      MinusInf.pretty  shouldBe "-Infinity"
      Undefined.pretty shouldBe "Undefined"
    }
  }

  trait Fixture {}

}
