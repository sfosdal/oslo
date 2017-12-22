package net.fosdal.oslo

import org.joda.time.DateTime

import scala.concurrent.duration._

package object odatetime {

  implicit val ordering: Ordering[DateTime] = Ordering.by(_.getMillis)

  implicit class DateTimeOps(private val dateTime: DateTime) extends AnyVal with Ordered[DateTime] {

    override def compare(that: DateTime): Int = Ordering[DateTime].compare(dateTime, that)

    def -(that: DateTime): FiniteDuration = (dateTime.getMillis - that.getMillis).milliseconds

    def +(duration: FiniteDuration): DateTime = dateTime.plus(duration.toMillis)

    def -(duration: FiniteDuration): DateTime = dateTime.minus(duration.toMillis)

  }

}
