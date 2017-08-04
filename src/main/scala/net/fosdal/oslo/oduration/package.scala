package net.fosdal.oslo

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

package object oduration {

  // FIXME change oslo to work on Durations rather than FiniteDurations
  // FIXME change oslo to allow an optional precision to be supplied
  // FIXME change oslo to allow an optional format to be supplied

  private[this] val abbr = Map(
    NANOSECONDS  -> "ns",
    MICROSECONDS -> "µs",
    MILLISECONDS -> "ms",
    SECONDS      -> "s",
    MINUTES      -> "m",
    HOURS        -> "h",
    DAYS         -> "d"
  )

  implicit class FiniteDurationOps(val d: FiniteDuration) extends AnyVal {

    private[this] def timeUnit(d: FiniteDuration): TimeUnit = {
      TimeUnit
        .values()
        .reverse
        .find(d.toUnit(_) >= 1)
        .getOrElse(TimeUnit.values().head)
    }

    def pretty: String = {
      val u = timeUnit(d)
      f"${d.toUnit(u)}%.1f${abbr(u)}"
    }

  }

}
