package net.fosdal.oslo

package object ostring {

  implicit class StringOps(val s: String) extends AnyVal {

    def toAlpha: String = s.replaceAll("""[^a-zA-Z]""", "")

    def toNumeric: String = s.replaceAll("""[^0-9\.\+\-]""", "")

    def toAlphanumeric: String = s.replaceAll("""[^a-zA-Z0-9\.\+\-]""", "")

  }

}
