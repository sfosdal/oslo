package net.fosdal.oslo

import scala.math.pow

package object ostring {

  private[this] val NotAlpha        = "[^a-zA-Z]"
  private[this] val NotNumeric      = "[^0-9]"
  private[this] val NotAlphaNumeric = "[^a-zA-Z0-9]"

  private[this] val ByteFactors = Seq("b", "kb", "mb", "gb", "tb", "pb", "eb").zipWithIndex
    .map(t => t._1 -> pow(1024D, t._2.toDouble))
    .toMap
  private[this] val BytePattern = "([0-9.]+?)\\s*([yzeptgmk]?b)".r

  implicit class StringOps(val s: String) extends AnyVal {

    def toAlpha: String = s.replaceAll(NotAlpha, "")

    def toNumeric: String = s.replaceAll(NotNumeric, "")

    def toAlphanumeric: String = s.replaceAll(NotAlphaNumeric, "")

    def asBytes: Long = {
      s.toLowerCase.trim match {
        case BytePattern(n, u) =>
          val d = n.trim.toDouble * ByteFactors(u.trim)
          if (math.floor(d) == d) {
            d.toLong
          } else {
            throw new NumberFormatException
          }
        case _ =>
          s.toLong
      }
    }

  }

}
