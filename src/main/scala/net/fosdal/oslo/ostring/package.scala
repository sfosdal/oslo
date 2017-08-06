package net.fosdal.oslo

package object ostring {

  private[this] val notAlpha   = """[^a-zA-Z]"""
  private[this] val notNumeric = """[^0-9]"""

  implicit class StringOps(val s: String) extends AnyVal {
    def toAlpha: String        = s.replaceAll(notAlpha, "")
    def toNumeric: String      = s.replaceAll(notNumeric, "")
    def toAlphanumeric: String = s.replaceAll(notAlpha, "").replaceAll(notNumeric, "")
  }

}
