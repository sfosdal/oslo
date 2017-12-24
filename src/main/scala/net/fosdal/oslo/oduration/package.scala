package net.fosdal.oslo

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._
import scala.math.signum

// TODO include more JodaTime conversions with scala.Duration/Interval/java.duration/etc ???
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
      case d: Duration if d == Zero     => "0ms"
      case d: Duration if d == Inf      => "Infinity"
      case d: Duration if d == MinusInf => "-Infinity"
      case d: FiniteDuration =>
        val u = timeUnit(d)
        s"${d.toUnit(u).formatted(s"%.${precision}f")}${abbr(u)}"
      case _: Duration => "Undefined"
    }
  }

  private[this] def timeUnit(d: FiniteDuration): TimeUnit = {
    TimeUnit
      .values()
      .reverse
      .find(d.abs.toUnit(_) >= 1)
      .get // FIXME given the definition of a FiniteDuration & TimeUnit this is safe
  }

  implicit class DurationOps(private val d: Duration) extends AnyVal {

    def pretty: String = pretty(1)

    def pretty(precision: Int): String = format(d, precision)

    def abs: Duration = {
      d match {
        case d1: FiniteDuration => d1 * signum(d1.toNanos)
        case Inf | MinusInf     => Inf
        case _                  => Undefined
      }
    }

    def toFiniteDuration: FiniteDuration = {
      d match {
        case fd: FiniteDuration => fd
        case _                  => throw new IllegalArgumentException(s"""unable to convert $d to FiniteDuration""")
      }
    }

    def toMaybeFiniteDuration: Option[FiniteDuration] = Some(d).collect { case d: FiniteDuration => d }

  }

}
