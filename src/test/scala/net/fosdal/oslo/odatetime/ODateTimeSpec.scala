package net.fosdal.oslo.odatetime

import org.joda.time.DateTime
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._

// scalastyle:off magic.number
class ODateTimeSpec extends WordSpec with Matchers with PropertyChecks {

  "ordering order collections correctly" in new Fixture {
    Seq(now, tomorrow, yesterday).sorted(ordering) shouldBe Seq(yesterday, now, tomorrow)
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
    val now       = new DateTime(1970, 5, 28, 18, 0, 0)
    val sameAsNow = now
    val tomorrow  = now.plusDays(1)
    val yesterday = now.minusDays(1)
  }

}
