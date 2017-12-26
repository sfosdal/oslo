package net.fosdal.oslo

import scala.annotation.tailrec

// scalastyle:off
package object onumber {

  private[this] val DefaultPrecision: Int = 1
  private[this] val DefaultMargin: Double = 0.9

  @tailrec
  private def fact(j: Long, result: Long = 1L): Long = {
    if (j == 0) {
      result
    } else if (j < 0) {
      throw new IllegalArgumentException("factorials of negative numbers not supported")
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

    def pow(exp: Int): Long = n.toLong.pow(exp)

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

    def pow(exp: Int): Long = pow(exp.toLong)

    def pow(exp: Long): Long = {

      @tailrec
      def _pow(acc: Long, b: Long, e: Long): Long = {
        if (e == 0L) {
          acc
        } else if ((e & 1) == 1) {
          _pow(acc * b, b * b, e >> 1L)
        } else {
          _pow(acc, b * b, e >> 1L)
        }
      }

      (n, exp) match {
        case (b, e) if b == 0L && e <= 0L =>
          throw new IllegalArgumentException(s"zero can only be raised to a positive power (exp=$exp)")
        case (b, _) if b == 0L                    => 0L
        case (b, _) if b == 1L                    => 1L
        case (b, e) if b == -1L && ((e & 1) == 0) => 1L
        case (b, e) if b == -1L && ((e & 1) == 1) => -1L
        case (_, e) if e < 0L                     => 0L
        case (b, e)                               => _pow(1L, b, e)
      }

    }

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
