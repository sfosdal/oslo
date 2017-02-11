package net.fosdal.oslo

import scala.annotation.tailrec
import net.fosdal.oslo.olong._

package object oint {

  implicit class IntOps(val i: Int) extends AnyVal {

    @tailrec
    private def fact(j: Int, result: Int = 1): Int = if (j == 0) result else fact(j - 1, j * result)

    def ! : Int = fact(i)

    def choose(k: Int): Int = fact(i) / (fact(i - k) * fact(k))

    def times(f: => Unit): Unit = (0 until i).foreach(_ => f)

    def pretty(size: Int): String = size.toLong.pretty

  }

}
