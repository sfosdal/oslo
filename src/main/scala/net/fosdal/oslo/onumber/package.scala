package net.fosdal.oslo

import scala.annotation.tailrec

package object onumber {

  private[this] val DefaultPrecision: Int = 1
  private[this] val DefaultMargin: Double = 0.9

  @tailrec
  private def fact(j: Long, result: Long = 1L): Long = {
    if (j == 0) {
      result
    } else if (j < 0) {
      throw new IllegalArgumentException("factorials of negative numbers not yet supported") // FIXME
    } else {
      fact(j - 1, j * result)
    }
  }

  implicit class IntOps(private val n: Int) extends AnyVal {

    def factorial: Long = fact(n.toLong)

    def choose(k: Int): Long = {
      if (k > n) {
        0L
      } else {
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

    def pretty(factor: Int = BytesPerKilobyte, precision: Int = DefaultPrecision, margin: Double = DefaultMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
    }

    def pretty: String = pretty()

  }

  implicit class LongOps(private val n: Long) extends AnyVal {

    def factorial: Long = fact(n)

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

    def pretty(factor: Int = BytesPerKilobyte, precision: Int = DefaultPrecision, margin: Double = DefaultMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
    }

    def pretty: String = pretty()

  }

  implicit class DoubleOps(private val n: Double) extends AnyVal {

    def pretty(factor: Int = BytesPerKilobyte, precision: Int = DefaultPrecision, margin: Double = DefaultMargin): String = {

      @tailrec
      def pretty(d: Double, u: Seq[String]): String = {
        val r = d / factor
        if (r < margin || u.lengthCompare(1) == 0) {
          d.formatted(s"%.${precision}f${u.head}")
        } else {
          pretty(r, u.tail)
        }
      }

      pretty(n, ByteUnits)
    }

    def pretty: String = pretty()

  }

}
