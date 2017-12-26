package net.fosdal.oslo

import net.fosdal.oslo.onumber._

package object ostring {

  private[this] val NotAlpha        = "[^a-zA-Z]"
  private[this] val NotNumeric      = "[^0-9]"
  private[this] val NotAlphaNumeric = "[^a-zA-Z0-9]"

  private[this] val BytePattern = s"([0-9.]+?)\\s*([${ByteUnits.tail.reverse.map(_.head).mkString}]?b)".r
  private[this] val ByteFactors = ByteUnits.zipWithIndex.toMap.mapValues(BytesPerKilobyte.pow)

  implicit class StringOps(private val s: String) extends AnyVal {

    /** removes all non-alpha characters from the [[scala.Predef.String]] (non-`[a-zA-Z]`)
      *
      * '''Example'''
      * {{{
      * scala> val foo = "a1b2c3!@#".toAlpha
      * foo: String = abc
      * }}}
      *
      * @version 0.3.0
      */
    def toAlpha: String = s.replaceAll(NotAlpha, "")

    /** removes all non-numeric characters from the [[scala.Predef.String]] (non-`[0-9]`)
      *
      * '''Example'''
      * {{{
      * scala> val foo = "a1b2c3!@#".toNumeric
      * foo: String = 123
      * }}}
      *
      * @version 0.3.0
      */
    def toNumeric: String = s.replaceAll(NotNumeric, "")

    /** removes all non-alphanumeric characters from the [[scala.Predef.String]] (non-`[0-9a-zA-Z]`)
      *
      * '''Example'''
      * {{{
      * scala> val foo = "a1b2c3!@#".toAlphanumeric
      * foo: String = a1b2c3
      * }}}
      *
      * @version 0.3.0
      */
    def toAlphanumeric: String = s.replaceAll(NotAlphaNumeric, "")

    /** Converts the [[scala.Predef.String]] representation of memory (e.g. `10mb`, `1.1kb`, etc.) to a [[scala.Long]]
      * representation of the number of bytes. It is forgiving of leading and trailing whitespace as well as whitespace
      * between the numerical portion and the unit of memory.
      *
      * If the [[scala.Predef.String]] does not match any known representation of memory, the fallback is to attempt to
      * convert the entire [[scala.Predef.String]] to a [[scala.Long]]
      *
      * For consistency, rounding to [[scala.Long]] is used in both of the above cases.
      *
      * '''Examples'''
      * {{{
      * scala> val foo = "1.5kb".asBytes
      * foo: Long = 1536
      *
      * // fall back using toLong
      * scala> val foo = "1100b".asBytes
      * foo: Long = 1100
      * scala> val foo = "1100".asBytes
      * foo: Long = 1100
      *
      * // rounding, would otherwise be 9341.952b
      * scala> val foo = "9.123kb".asBytes
      * foo: Long = 9342
      *
      * // fallback rounding, for consistency
      * scala> val foo = "9.1b".asBytes
      * foo: Long = 9
      * scala> val foo = "9.1".asBytes
      * foo: Long = 9
      * }}}
      *
      * @throws java.lang.NumberFormatException - If the string does not contain a memory representation that
      *                                         is parsable to a [[scala.Long]]
      * @version 0.3.0
      */
    @throws(classOf[NumberFormatException])
    def asBytes: Long = {
      s.toLowerCase.trim match {
        case BytePattern(n, u) =>
          math.round(n.trim.toDouble * ByteFactors(u.trim))
        case _ =>
          math.round(s.toDouble)
      }
    }

  }

}
