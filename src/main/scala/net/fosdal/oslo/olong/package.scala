package net.fosdal.oslo

import scala.annotation.tailrec

package object olong {

  implicit class LongOps(val n: Long) extends AnyVal {

    @tailrec
    private def fact(j: Long, result: Long = 1L): Long = if (j == 0) result else fact(j - 1, j * result)

    def ! : Long = fact(n)

    def choose(k: Long): Long = fact(n) / (fact(n - k) * fact(k))

    def times(f: => Unit): Unit = {
      @tailrec
      def times(j: Long, f: => Unit): Unit = {
        if (j > 0) {
          f
          times(j - 1, f)
        }
      }

      times(n, f)
    }

    def prettyInBytes: String = {
      val K = 1024L
      val M = K * K
      val G = M * K
      val T = G * K
      val P = T * K
      val E = P * K
      val f = 0.9
      val dbl = n.toDouble
      val (v: Double, u: String) = {
        if (dbl / f < K) {
          (dbl, "B")
        } else if (dbl / f < M) {
          (dbl / K, "KB")
        } else if (dbl / f < G) {
          (dbl / M, "MB")
        } else if (dbl / f < T) {
          (dbl / G, "GB")
        } else if (dbl / f < P) {
          (dbl / T, "TB")
        } else if (dbl / f < E) {
          (dbl / P, "PB")
        } else {
          (dbl / E, "EB")
        }
      }
      if (u == "B") {
        f"$v%.0f$u%s"
      } else {
        f"$v%.1f$u%s"
      }
    }

    def pretty: String = {
      val K   = 1000L
      val M   = K * K
      val G   = M * K
      val T   = G * K
      val P   = T * K
      val E   = P * K
      val f   = 0.9
      val dbl = n.toDouble
      val (v: Double, u: String) = {
        if (dbl / f < K) {
          (dbl, "")
        } else if (dbl / f < M) {
          (dbl / K, "k") // fix abbreviations
        } else if (dbl / f < G) {
          (dbl / M, "M")
        } else if (dbl / f < T) {
          (dbl / G, "G")
        } else if (dbl / f < P) {
          (dbl / T, "T")
        } else if (dbl / f < E) {
          (dbl / P, "P")
        } else {
          (dbl / E, "E")
        }
      }
      if (u == "") {
        f"$v%.0f$u%s"
      } else {
        f"$v%.1f$u%s"
      }
    }
  }

}
