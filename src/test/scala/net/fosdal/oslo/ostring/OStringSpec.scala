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

  trait Fixture {
    val alpha            = """[a-zA-Z]"""
    val onlyAlpha        = s"""^$alpha*$$"""
    val numeric          = """[0-9]"""
    val onlyNumeric      = s"""^$numeric*$$"""
    val alphanumeric     = """[a-zA-Z0-9]"""
    val onlyAlphanumeric = s"""^$alphanumeric*$$"""
  }

}
