package net.fosdal.oslo

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration._
import scala.concurrent.duration._
import scala.language.implicitConversions
import scala.math.signum

package object oduration {

  private[this] val DefaultTimeUnits = MILLISECONDS

  private[this] val abbr = Map(
    NANOSECONDS -> "ns",
    MICROSECONDS -> "µs",
    MILLISECONDS -> "ms",
    SECONDS -> "s",
    MINUTES -> "m",
    HOURS -> "h",
    DAYS -> "d"
  )

  private[this] def format(duration: Duration, precision: Int): String = {
    duration match {
      case d: Duration if d == Inf => "Infinity"
      case d: Duration if d == MinusInf => "-Infinity"
      case d: FiniteDuration =>
        val u = timeUnit(d)
        s"${d.toUnit(u).formatted(s"%.${precision}f")}${abbr(u)}"
      case _: Duration => "Undefined"
    }
  }

  private[this] def timeUnit(d: FiniteDuration): TimeUnit = {
    val units = TimeUnit.values()
    units.reverse
      .find(d.abs.toUnit(_) >= 1)
      .getOrElse(DefaultTimeUnits)
  }

  def abs(d: Duration): Duration = d.abs

  def pretty(d: Duration): String = d.pretty

  def pretty(d: Duration, precision: Int): String = d.pretty(precision)

  implicit def asDuration(d: org.joda.time.Duration): Duration = d.getMillis.milliseconds

  implicit def asDuration(i: org.joda.time.Interval): Duration = asDuration(i.toDuration)

  implicit def asFiniteDuration(d: Duration): FiniteDuration = {
    d match {
      case fd: FiniteDuration => fd
      case _ => throw new IllegalArgumentException(s"""unable to convert $d to FiniteDuration""")
    }
  }

  implicit class DurationOps(private val d: Duration) extends AnyVal {

    def pretty: String = pretty(1)

    def pretty(precision: Int): String = format(d, precision)

    def abs: Duration = {
      d match {
        case d1: FiniteDuration => d1 * signum(d1.toNanos)
        case Inf | MinusInf => Inf
        case _ => Undefined
      }
    }

    def toFiniteDuration: Option[FiniteDuration] = Some(d).collect { case d: FiniteDuration => d }

  }

}
