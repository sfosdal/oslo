package net.fosdal.oslo.ostring

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class OStringSpec extends WordSpec with Matchers with PropertyChecks {

  "toAlpha" when {
    "when given arbitrary strings" must {
      "return results that are [a-Z][A-Z]" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.toAlpha.matches(onlyAlpha) shouldBe true)
        }
      }
    }
    "when given arbitrary strings with all [a-z][A-Z] removed" must {
      "return an empty string" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.replaceAll(alpha, "").toAlpha shouldBe 'empty)
        }
      }
    }
  }

  "toNumeric" when {
    "when given arbitrary strings" must {
      "return results that are [0-9]" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.toNumeric.matches(onlyNumeric) shouldBe true)
        }
      }
    }
    "when given arbitrary strings with all [0-9] removed" must {
      "return an empty string" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.replaceAll(numeric, "").toNumeric shouldBe 'empty)
        }
      }
    }
  }

  "toAlphanumeric" when {
    "when given arbitrary strings" must {
      "return results that are [a-Z][A-Z][0-9]" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.toAlphanumeric.matches(onlyAlphanumeric) shouldBe true)
        }
      }
    }
    "when given arbitrary strings with all [a-Z][A-Z][0-9] removed" must {
      "return an empty string" in new Fixture {
        Seq(arbString.arbitrary, alphaStr, alphaNumStr, numStr) foreach { gen =>
          forAll(gen)(_.replaceAll(alphanumeric, "").toAlphanumeric shouldBe 'empty)
        }
      }
    }
  }

  "asBytes" must {
    "parse standard formats properly" in {
      "1b".asBytes shouldBe 1L
      "2000b".asBytes shouldBe 2000L
      "1.5Kb".asBytes shouldBe 1500L
      "2000kB".asBytes shouldBe 2000000L
      "1.5mb".asBytes shouldBe 1500000L
      "2000mb".asBytes shouldBe 2000000000L
      "1.5gb".asBytes shouldBe 1500000000L
      "2000GB".asBytes shouldBe 2000000000000L
      "1.5tb".asBytes shouldBe 1500000000000L
      "2000tb".asBytes shouldBe 2000000000000000L
      "1.5pb".asBytes shouldBe 1500000000000000L
      "2000pb".asBytes shouldBe 2000000000000000000L
      "1.5EB".asBytes shouldBe 1500000000000000000L
      "2000eb".asBytes shouldBe 9223372036854775807L
      "8eb".asBytes shouldBe 8000000000000000000L
    }
    "round decimal kilobytes" in {
      "9.123kb".asBytes shouldBe 9123L
      "9.1b".asBytes shouldBe 9L
      "9.1".asBytes shouldBe 9L
      "9.5b".asBytes shouldBe 10L
      "9.5".asBytes shouldBe 10L
    }

    "be forgiving" in {
      "  1  b  ".asBytes shouldBe 1L
    }
    "fall back to toLong" in {
      "9".asBytes shouldBe 9L
    }
    "fail properly" when {
      "given unknown units" in {
        intercept[NumberFormatException] {
          "9xxx".asBytes
        }
      }
    }
  }

  trait Fixture {
    val alpha = """[a-zA-Z]"""
    val onlyAlpha = s"""^$alpha*$$"""
    val numeric = """[0-9]"""
    val onlyNumeric = s"""^$numeric*$$"""
    val alphanumeric = """[a-zA-Z0-9]"""
    val onlyAlphanumeric = s"""^$alphanumeric*$$"""
  }

}
