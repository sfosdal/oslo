package net.fosdal.oslo.onumber

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

// scalastyle:off magic.number
class ONumberSpec extends WordSpec with Matchers with PropertyChecks {

  "factorial" must {
    "throw exceptions for negative numbers" in {
      an[IllegalArgumentException] should be thrownBy {
        (-1).factorial shouldBe 1L
      }
      an[IllegalArgumentException] should be thrownBy {
        (-1L).factorial shouldBe 1L
      }
    }
    "work for Ints" in {
      0.factorial shouldBe 1L
      5.factorial shouldBe 120L
      15.factorial shouldBe 1307674368000L
    }
    "work for Longs" in {
      0L.factorial shouldBe 1L
      5L.factorial shouldBe 120L
      17L.factorial shouldBe 355687428096000L
    }
  }

  "choose" must {
    "work for Ints" in {
      0.choose(5) shouldBe 0L
      10.choose(0) shouldBe 1L
      10.choose(3) shouldBe 120L
    }
    "work for Longs" in {
      0L.choose(5L) shouldBe 0L
      10L.choose(0L) shouldBe 1L
      20L.choose(3L) shouldBe 1140L
    }
  }

  "times" must {
    "throw exception for negative Ints" in {
      an[IllegalArgumentException] should be thrownBy {
        val n: Int = -5
        var i: Int = 0
        n.times {
          i = i + 1
        }
        i shouldBe n
      }
    }
    "throw exception for negative Longs" in {
      an[IllegalArgumentException] should be thrownBy {
        val n: Long = -3
        var i: Long = 0
        n.times {
          i = i + 1
        }
        i shouldBe n
      }
    }
    "work for Ints" in {
      val n: Int = 10
      var i: Int = 0
      n.times {
        i = i + 1
      }
      i shouldBe n
    }
    "work for Longs" in {
      val n: Long = 1000L
      var i: Long = 0L
      n.times {
        i = i + 1
      }
      i shouldBe n
    }
  }

  "pretty" must {
    "make Ints pretty" in {
      (-1).pretty shouldBe "-1.0b"
      0.pretty shouldBe "0.0b"
      899.pretty shouldBe "899.0b"
      900.pretty shouldBe "0.9kb"
      5000.pretty shouldBe "5.0kb"
      1220000.pretty shouldBe "1.2mb"
      1234567890.pretty shouldBe "1.2gb"
      Int.MaxValue.pretty shouldBe "2.1gb"
    }
    "make Longs pretty" in {
      (-1L).pretty shouldBe "-1.0b"
      0L.pretty shouldBe "0.0b"
      899L.pretty shouldBe "899.0b"
      900L.pretty shouldBe "0.9kb"
      5000L.pretty shouldBe "5.0kb"
      1220000L.pretty shouldBe "1.2mb"
      1234567890L.pretty shouldBe "1.2gb"
      25894500067890L.pretty shouldBe "25.9tb"
      18304567800000090L.pretty shouldBe "18.3pb"
      Long.MaxValue.pretty shouldBe "9.2eb"
    }
    "make Doubles pretty" in {
      (-1D).pretty shouldBe "-1.0b"
      0.5D.pretty shouldBe "0.5b"
      0D.pretty shouldBe "0.0b"
      899D.pretty shouldBe "899.0b"
      900D.pretty shouldBe "0.9kb"
      5000D.pretty shouldBe "5.0kb"
      1220000D.pretty shouldBe "1.2mb"
      1234567890D.pretty shouldBe "1.2gb"
      25894500067890D.pretty shouldBe "25.9tb"
      18304567800000090D.pretty shouldBe "18.3pb"
      Long.MaxValue.pretty shouldBe "9.2eb"
    }

  }
  "pow" when {
    "using zero base" must {
      "handle the exceptional cases" in {
        an[IllegalArgumentException] should be thrownBy {
          0L.pow(-1L)
        }
        an[IllegalArgumentException] should be thrownBy {
          0L.pow(0L) shouldBe 1L
        }
      }
      "handle the positive exp cases" in {
        0L.pow(1L) shouldBe 0L
        0L.pow(2L) shouldBe 0L
        0L.pow(7L) shouldBe 0L
        0L.pow(100L) shouldBe 0L
        0L.pow(Long.MaxValue) shouldBe 0L
      }
    }
    "using -1 base" must {
      "handle even case" in {
        (-1L).pow(0L) shouldBe 1L
        (-1L).pow(2L) shouldBe 1L
        (-1L).pow(4L) shouldBe 1L
        (-1L).pow(6L) shouldBe 1L
      }
      "handle odd case" in {
        (-1L).pow(3L) shouldBe -1L
        (-1L).pow(5L) shouldBe -1L
        (-1L).pow(7L) shouldBe -1L
      }
    }
    "using other non-zero bases" must {
      "handle exp = 0L" in {
        1L.pow(0L) shouldBe 1L
        2L.pow(0L) shouldBe 1L
        7L.pow(0L) shouldBe 1L
        100L.pow(0L) shouldBe 1L
        Long.MaxValue.pow(0L) shouldBe 1L
      }
      "handle exp > 0L" in {
        1L.pow(3L) shouldBe 1L
        2L.pow(7L) shouldBe 128L
        7L.pow(11L) shouldBe 1977326743L
        100L.pow(12L) shouldBe 2003764205206896640L
      }
      "handle exp < 0L" in {
        1L.pow(-3L) shouldBe 1L
        2L.pow(-7L) shouldBe 0L
        7L.pow(-11L) shouldBe 0L
        100L.pow(-12L) shouldBe 0L
      }
    }

  }
}
