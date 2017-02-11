package net.fosdal.oslo

import java.util.concurrent.TimeUnit

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

  implicit class FiniteDurationOps(val d: FiniteDuration) extends AnyVal {

    private[this] def unit(d: FiniteDuration): TimeUnit = {
      TimeUnit
        .values()
        .reverse
        .collectFirst({ case unit: TimeUnit if d.toUnit(unit) >= 1 => unit })
        .getOrElse(TimeUnit.values().head)
    }

    def pretty: String = {
      val u = unit(d)
      f"${d.toUnit(u)}%.1f ${u.name.toLowerCase}"
    }

    def prettyAbbr: String = {
      val u = unit(d)
      f"${d.toUnit(u)}%.1f${abbr(u)}"
    }

  }

}
