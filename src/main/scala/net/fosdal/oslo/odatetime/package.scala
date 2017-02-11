package net.fosdal.oslo

import org.joda.time.DateTime

import scala.concurrent.duration._

package object odatetime {

  implicit val ordering = Ordering.by[DateTime, Long](_.getMillis)

  def max(d1: DateTime,d2: DateTime): DateTime = if (d1 >= d2) d1 else d2

  def min(d1: DateTime,d2: DateTime): DateTime = if (d1 <= d2) d1 else d2

  implicit class DateTimeOps(val dateTime: DateTime) extends AnyVal with Ordered[DateTime] {

    override def compare(that: DateTime): Int = Ordering[DateTime].compare(dateTime, that)

    def -(that: DateTime): FiniteDuration = (dateTime.getMillis - that.getMillis).milliseconds

    def +(duration: FiniteDuration): DateTime = dateTime.plus(duration.toMillis)

    def -(duration: FiniteDuration): DateTime = dateTime.minus(duration.toMillis)

    def min(that: DateTime): DateTime = if (this <= that) this.dateTime else that

    def max(that: DateTime): DateTime = if (this >= that) this.dateTime else that

  }

}
