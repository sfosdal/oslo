package net.fosdal.oslo

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._

package object oduration {

  private[this] val abbr = Map(
    NANOSECONDS  -> "ns",
    MICROSECONDS -> "µs",
    MILLISECONDS -> "ms",
    SECONDS      -> "s",
    MINUTES      -> "m",
    HOURS        -> "h",
    DAYS         -> "d"
  )

  private[this] def format(duration: Duration, precision: Int): String = {
    duration match {
      case d: Duration if d == Zero => "0ms"
      case d: FiniteDuration =>
        val u = timeUnit(d)
        s"${d.toUnit(u).formatted(s"%.${precision}f")}${abbr(u)}"
      case d: Duration if d == Inf      => "Infinity"
      case d: Duration if d == MinusInf => "-Infinity"
      case _: Duration                  => "Undefined"
    }
  }

  private[this] def timeUnit(d: Duration): TimeUnit = {
    TimeUnit
      .values()
      .reverse
      .find(d.toUnit(_) >= 1)
      .getOrElse(TimeUnit.values().head)
  }

  implicit class DurationOps(val d: Duration) extends AnyVal {

    def pretty: String = pretty()

    def pretty(precision: Int = 1): String = format(d, precision)

  }

}
