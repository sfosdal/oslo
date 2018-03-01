package net.fosdal.oslo.odatetime

import org.joda.time.DateTimeZone.UTC
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import org.scalacheck.Gen.chooseNum
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._

// scalastyle:off magic.number
class ODateTimeSpec extends WordSpec with Matchers with PropertyChecks {

  "LocalDateOps" in new Fixture {
    val d1 = LocalDate.now
    val d2 = d1.plusDays(2)
    d1 > d2 shouldBe false
  }

  "DateTimeOps" in new Fixture {
    tomorrow > yesterday shouldBe true
  }

  "orderingDateTime order collections correctly" in new Fixture {
    Seq(now, tomorrow, yesterday).sorted(orderingDateTime) shouldBe Seq(yesterday, now, tomorrow)
  }

  "orderingLocalDate order collections correctly" in new Fixture {
    val input    = Seq(now, tomorrow, yesterday).map(_.toLocalDate)
    val expected = Seq(yesterday, now, tomorrow).map(_.toLocalDate)
    input.sorted(orderingLocalDate) shouldBe expected
  }

  "floor" must {
    "support static form" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      floor(dt, 8.hours) shouldBe new DateTime(1970, 5, 28, 16, 0, 0, 0, UTC)
    }
    "handle datetimes in non-utc zones" in {
      val dt1 = new DateTime(1970, 5, 28, 18, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt1.floor(1.day) shouldBe new DateTime(1970, 5, 28, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
      val dt2 = new DateTime(1970, 5, 28, 11, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt2.floor(1.day) shouldBe new DateTime(1970, 5, 28, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
    }
    "handle datetimes" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      dt.floor(1.day) shouldBe new DateTime(1970, 5, 28, 0, 0, 0, 0, UTC)
      dt.floor(8.hours) shouldBe new DateTime(1970, 5, 28, 16, 0, 0, 0, UTC)
      dt.floor(1.hour) shouldBe new DateTime(1970, 5, 28, 18, 0, 0, 0, UTC)
      dt.floor(30.minutes) shouldBe new DateTime(1970, 5, 28, 18, 30, 0, 0, UTC)
      dt.floor(1.minute) shouldBe new DateTime(1970, 5, 28, 18, 35, 0, 0, UTC)
      dt.floor(30.seconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 0, 0, UTC)
      dt.floor(1.second) shouldBe new DateTime(1970, 5, 28, 18, 35, 12, 0, UTC)
      dt.floor(100.milliseconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 12, 300, UTC)
      dt.floor(1.millisecond) shouldBe dt
      an[IllegalArgumentException] should be thrownBy {
        dt.floor(-1.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.floor(0.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.floor(999.microseconds)
      }
    }
  }

  "ceil" must {
    "support static form" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      ceil(dt, 8.hours) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, UTC)
    }
    "handle datetimes in non-utc zones" in {
      val dt1 = new DateTime(1970, 5, 28, 18, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt1.ceil(1.day) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
      val dt2 = new DateTime(1970, 5, 28, 11, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt2.ceil(1.day) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
    }
    "handle datetimes" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      dt.ceil(1.day) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, UTC)
      dt.ceil(8.hours) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, UTC)
      dt.ceil(1.hour) shouldBe new DateTime(1970, 5, 28, 19, 0, 0, 0, UTC)
      dt.ceil(30.minutes) shouldBe new DateTime(1970, 5, 28, 19, 0, 0, 0, UTC)
      dt.ceil(1.minute) shouldBe new DateTime(1970, 5, 28, 18, 36, 0, 0, UTC)
      dt.ceil(30.seconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 30, 0, UTC)
      dt.ceil(1.second) shouldBe new DateTime(1970, 5, 28, 18, 35, 13, 0, UTC)
      dt.ceil(100.milliseconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 12, 400, UTC)
      dt.ceil(1.millisecond) shouldBe dt
      an[IllegalArgumentException] should be thrownBy {
        dt.ceil(-1.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.ceil(0.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.ceil(999.microseconds)
      }
    }
  }

  "utcOffset" in new Fixture {
    forAll(chooseNum(-MaxTZMillis, MaxTZMillis)) { i: Int =>
      val tz = DateTimeZone.forOffsetMillis(i)
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, tz)
      dt.utcOffset shouldBe i.millis
    }
  }

  "timezoneOffset" must {
    "match utcOffset for UTC" in new Fixture {
      forAll(chooseNum(-MaxTZMillis, MaxTZMillis)) { i: Int =>
        val tz = DateTimeZone.forOffsetMillis(i)
        val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, tz)
        dt.timezoneOffset(UTC) shouldBe dt.utcOffset
      }
    }
    "work for non-utc zones" in new Fixture {
      forAll(chooseNum(-MaxTZMillis, MaxTZMillis), chooseNum(-MaxTZMillis, MaxTZMillis)) {
        case (i: Int, j: Int) =>
          val tz1 = DateTimeZone.forOffsetMillis(i)
          val tz2 = DateTimeZone.forOffsetMillis(j)
          val dt  = new DateTime(1970, 5, 28, 18, 35, 12, 345, tz1)
          dt.timezoneOffset(tz2) shouldBe (i - j).millis
      }
    }
  }

  "round" must {
    "support static form" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      round(dt, 8.hours) shouldBe new DateTime(1970, 5, 28, 16, 0, 0, 0, UTC)
    }
    "handle datetimes in non-utc zones" in {
      val dt1 = new DateTime(1970, 5, 28, 18, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt1.round(1.day) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
      val dt2 = new DateTime(1970, 5, 28, 11, 35, 12, 345, DateTimeZone.forOffsetHours(7))
      dt2.round(1.day) shouldBe new DateTime(1970, 5, 28, 0, 0, 0, 0, DateTimeZone.forOffsetHours(7))
    }
    "handle datetimes" in {
      val dt = new DateTime(1970, 5, 28, 18, 35, 12, 345, UTC)
      dt.round(1.day) shouldBe new DateTime(1970, 5, 29, 0, 0, 0, 0, UTC)
      dt.round(8.hours) shouldBe new DateTime(1970, 5, 28, 16, 0, 0, 0, UTC)
      dt.round(1.hour) shouldBe new DateTime(1970, 5, 28, 19, 0, 0, 0, UTC)
      dt.round(30.minutes) shouldBe new DateTime(1970, 5, 28, 18, 30, 0, 0, UTC)
      dt.round(1.minute) shouldBe new DateTime(1970, 5, 28, 18, 35, 0, 0, UTC)
      dt.round(30.seconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 0, 0, UTC)
      dt.round(1.second) shouldBe new DateTime(1970, 5, 28, 18, 35, 12, 0, UTC)
      dt.round(100.milliseconds) shouldBe new DateTime(1970, 5, 28, 18, 35, 12, 300, UTC)
      dt.round(1.millisecond) shouldBe dt
      an[IllegalArgumentException] should be thrownBy {
        dt.round(-1.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.round(0.day)
      }
      an[IllegalArgumentException] should be thrownBy {
        dt.round(999.microseconds)
      }
    }
  }

  "comparisons" in new Fixture {
    yesterday < now shouldBe true
    yesterday > now shouldBe false
    sameAsNow <= now shouldBe true
    sameAsNow >= now shouldBe true
  }

  "Adding FiniteDuration to DateTime" in new Fixture {
    forAll { millis: Int =>
      now + millis.milliseconds shouldBe now.plusMillis(millis)
    }
  }

  "Subtracting FiniteDuration from DateTime" in new Fixture {
    forAll { millis: Int =>
      now - millis.milliseconds shouldBe now.minusMillis(millis)
    }
  }

  "Subtracting DateTime from DateTime" in new Fixture {
    tomorrow - yesterday shouldBe 2.days
    yesterday - tomorrow shouldBe -2.days
  }

  trait Fixture {
    val MaxTZMillis = (86400 * 1000) - 1
    val now         = new DateTime(1970, 5, 28, 18, 0, 0)
    val sameAsNow   = now
    val tomorrow    = now.plusDays(1)
    val yesterday   = now.minusDays(1)
  }

}
