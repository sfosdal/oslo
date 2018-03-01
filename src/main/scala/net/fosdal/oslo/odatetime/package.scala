package net.fosdal.oslo

import net.fosdal.oslo.oduration._
import org.joda.time.DateTimeZone._
import org.joda.time.{DateTime, DateTimeZone, LocalDate}

import scala.concurrent.duration.Duration.Zero
import scala.concurrent.duration._

package object odatetime {

  private[this] def gen(dt: DateTime, fd: FiniteDuration, f: Double => Double): DateTime = {
    fd match {
      case d if d < Zero =>
        throw new IllegalArgumentException(s"does not support negative durations ($d)")
      case d if d < 1.millisecond =>
        throw new IllegalArgumentException(s"does not support sub-millisecond durations ($d)")
      case d =>
        val offset = dt.utcOffset.toMillis
        val n      = d.toMillis
        val x      = (f((dt.getMillis.toDouble + offset) / n) * n).toLong - offset
        new DateTime(x, dt.getZone)
    }
  }

  def floor(dateTime: DateTime, x: FiniteDuration): DateTime = gen(dateTime, x, math.floor)

  def ceil(dateTime: DateTime, x: FiniteDuration): DateTime = gen(dateTime, x, math.ceil)

  def round(dateTime: DateTime, x: FiniteDuration): DateTime = gen(dateTime, x, math.round(_).toDouble)

  implicit val orderingDateTime: Ordering[DateTime] = Ordering.by(_.getMillis)

  implicit val orderingLocalDate: Ordering[LocalDate] = Ordering.by(d => (d.getYear, d.getDayOfYear))

  implicit class LocalDateOps(private val localDate: LocalDate) extends AnyVal with Ordered[LocalDate] {
    override def compare(that: LocalDate): Int = Ordering[LocalDate].compare(localDate, that)
  }

  implicit class DateTimeOps(private val dateTime: DateTime) extends AnyVal with Ordered[DateTime] {

    override def compare(that: DateTime): Int = Ordering[DateTime].compare(dateTime, that)

    def -(that: DateTime): FiniteDuration = (dateTime.getMillis - that.getMillis).milliseconds

    def +(duration: FiniteDuration): DateTime = dateTime.plus(duration.toMillis)

    def -(duration: FiniteDuration): DateTime = dateTime.minus(duration.toMillis)

    def floor(x: FiniteDuration): DateTime = gen(dateTime, x, math.floor)

    def ceil(x: FiniteDuration): DateTime = gen(dateTime, x, math.ceil)

    def round(x: FiniteDuration): DateTime = gen(dateTime, x, math.round(_).toDouble)

    def utcOffset: FiniteDuration = {
      dateTime.getZone
        .getOffset(DateTime.now(UTC))
        .milliseconds
        .toCoarsest
        .toFiniteDuration
    }

    def timezoneOffset(tz: DateTimeZone): FiniteDuration = dateTime.utcOffset - DateTime.now(tz).utcOffset

  }

}
