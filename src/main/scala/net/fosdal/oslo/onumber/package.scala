package net.fosdal.oslo

import scala.annotation.tailrec

package object onumber {

  private[this] val DefaultPrettyFactor: Int    = 1000
  private[this] val DefaultPrettyPrecision: Int = 1
  private[this] val DefaultPrettyMargin: Double = 0.9

  @tailrec
  private def f(j: Long, result: Long = 1L): Long = {
    if (j == 0) {
      result
    } else if (j < 0) {
      throw new IllegalArgumentException("factorials of negative numbers not yet supported")
    } else {
      f(j - 1, j * result)
    }
  }

  implicit class IntOps(val n: Int) extends AnyVal {
    def factorial: Long = f(n.toLong)

    def choose(k: Int): Long = {
      if (k > n) {
        0L
      } else {
        // TODO known issue, this does not work for high values
        n.factorial / ((n - k).factorial * k.factorial)
      }
    }

    def times(f: => Unit): Unit = {
      if (n < 0) {
        throw new IllegalArgumentException(s"cannot do f $n times")
      } else {
        (0 until n).foreach(_ => f)
      }
    }

    def pretty(factor: Int = DefaultPrettyFactor,
               precision: Int = DefaultPrettyPrecision,
               margin: Double = DefaultPrettyMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
    }

    def pretty: String = pretty(DefaultPrettyFactor, DefaultPrettyPrecision, DefaultPrettyMargin)

  }

  implicit class LongOps(val n: Long) extends AnyVal {
    def factorial: Long = f(n)

    def choose(k: Long): Long = {
      if (k > n) {
        0L
      } else {
        n.factorial / ((n - k).factorial * k.factorial)
      }
    }

    def times(f: => Unit): Unit = {
      @tailrec
      def times(j: Long, f: => Unit): Unit = {
        if (j > 0) {
          f
          times(j - 1, f)
        }
      }

      if (n < 0) {
        throw new IllegalArgumentException(s"cannot do f $n times")
      } else {
        times(n, f)
      }
    }

    def pretty(factor: Int = DefaultPrettyFactor,
               precision: Int = DefaultPrettyPrecision,
               margin: Double = DefaultPrettyMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
    }

    def pretty: String = pretty(DefaultPrettyFactor, DefaultPrettyPrecision, DefaultPrettyMargin)

  }

  implicit class DoubleOps(val n: Double) extends AnyVal {

    def pretty(factor: Int = DefaultPrettyFactor,
               precision: Int = DefaultPrettyPrecision,
               margin: Double = DefaultPrettyMargin): String = {
      val units = Seq("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")

      @tailrec
      def pretty(d: Double, u: Seq[String]): String = {
        val r = d / factor
        if (r < margin || u.length == 1) {
          d.formatted(s"%.${precision}f${u.head}")
        } else {
          pretty(r, u.tail)
        }
      }

      pretty(n, units)
    }

    def pretty: String = pretty(DefaultPrettyFactor, DefaultPrettyPrecision, DefaultPrettyMargin)

  }

}
