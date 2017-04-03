package net.fosdal.oslo

import java.text.DecimalFormat

import scala.annotation.tailrec

package object odouble {

  private[this] val DefaultPrettyFactor = 1000

  implicit class DoubleOps(val d: Double) extends AnyVal {

    def pretty(factor: Int): String = {
      val margin = 0.9D
      val units  = Seq("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
      val format = "%.2f"

      val formatter = new DecimalFormat(format)

      @tailrec
      def pretty(d: Double, u: Seq[String]): String = {
        val r = d / factor
        if (r < margin || u.length == 1) {
          s"${formatter.format(d)}${u.head}"
        } else {
          pretty(r, u.tail)
        }
      }

      pretty(d, units)
    }

    def pretty: String = pretty(DefaultPrettyFactor)

  }

}
