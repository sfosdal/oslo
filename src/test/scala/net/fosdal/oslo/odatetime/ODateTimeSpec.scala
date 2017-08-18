package net.fosdal.oslo.odatetime

import org.joda.time.DateTime
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Ignore, Matchers, WordSpec}

import scala.concurrent.duration._

class ODateTimeSpec extends WordSpec with Matchers with PropertyChecks {

  "ordering order collections correctly" in new Fixture {
    Seq(now, tomorrow, yesterday).sorted(ordering) shouldBe Seq(yesterday, now, tomorrow)
  }

  "comparisons" in new Fixture {
    yesterday < now  shouldBe true
    yesterday > now  shouldBe false
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
    val now       = DateTime.now
    val sameAsNow = DateTime.now
    val tomorrow  = now.plusDays(1)
    val yesterday = now.minusDays(1)
  }

}
