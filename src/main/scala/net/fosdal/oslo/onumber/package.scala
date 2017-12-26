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
      throw new IllegalArgumentException("factorials of negative numbers not supported")
    } else {
      fact(j - 1, j * result)
    }
  }

  def factorial(i: Int): Long = i.factorial

  def factorial(i: Long): Long = i.factorial

  def choose(i: Int, k: Int): Long = i.choose(k)

  def choose(i: Long, k: Int): Long = i.choose(k)

  def choose(i: Int, k: Long): Long = i.choose(k)

  def choose(i: Long, k: Long): Long = i.choose(k)

  def times(i: Int)(f: => Unit): Unit = i.times(f)

  def times(i: Long)(f: => Unit): Unit = i.times(f)

  def pow(i: Int, exp: Int): Long = i.pow(exp)

  def pow(i: Long, exp: Int): Long = i.pow(exp)

  def pow(i: Long, exp: Long): Long = i.pow(exp)

  def pretty(i: Int): String = i.pretty

  def pretty(i: Long): String = i.pretty

  implicit class IntOps(private val n: Int) extends AnyVal {

    def factorial: Long = fact(n.toLong)

    def choose(k: Long): Long = n.toLong.choose(k)

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

    def pow(exp: Int): Long = n.toLong.pow(exp)

    def pretty: String = pretty()

    def pretty(factor: Int = BytesPerKilobyte, precision: Int = DefaultPrecision, margin: Double = DefaultMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
    }

  }

  implicit class LongOps(private val n: Long) extends AnyVal {

    def factorial: Long = fact(n)

    def choose(k: Int): Long = choose(k.toLong)

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

    def pow(exp: Int): Long = pow(exp.toLong)

    def pow(exp: Long): Long = { // scalastyle:off cyclomatic.complexity

      @tailrec
      def pow(result: Long, base: Long, exp: Long): Long = {
        exp match {
          case 0L                  => result
          case e if (exp & 1) == 1 => pow(result * base, base * base, e >> 1L)
          case e                   => pow(result, base * base, e >> 1L)
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
        case (b, e)                               => pow(1L, b, e)
      }

    }

    def pretty: String = pretty()

    def pretty(factor: Int = BytesPerKilobyte, precision: Int = DefaultPrecision, margin: Double = DefaultMargin): String = {
      n.toDouble.pretty(factor, precision, margin)
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
