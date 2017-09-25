package net.fosdal.oslo.onumber

import org.scalatest.{Matchers, WordSpec}

// scalastyle:off magic.number
class ONumberSpec extends WordSpec with Matchers {

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
      0.factorial  shouldBe 1L
      5.factorial  shouldBe 120L
      15.factorial shouldBe 1307674368000L
    }
    "work for Longs" in {
      0L.factorial  shouldBe 1L
      5L.factorial  shouldBe 120L
      17L.factorial shouldBe 355687428096000L
    }
  }

  "choose" must {
    "work for Ints" in {
      0.choose(5)  shouldBe 0L
      10.choose(0) shouldBe 1L
      10.choose(3) shouldBe 120L
    }
    "work for Longs" in {
      0L.choose(5L)  shouldBe 0L
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
      (-1).pretty         shouldBe "-1.0B"
      0.pretty            shouldBe "0.0B"
      899.pretty          shouldBe "899.0B"
      900.pretty          shouldBe "0.9KB"
      5000.pretty         shouldBe "5.0KB"
      1220000.pretty      shouldBe "1.2MB"
      1234567890.pretty   shouldBe "1.2GB"
      Int.MaxValue.pretty shouldBe "2.1GB"
    }
    "make Longs pretty" in {
      (-1L).pretty              shouldBe "-1.0B"
      0L.pretty                 shouldBe "0.0B"
      899L.pretty               shouldBe "899.0B"
      900L.pretty               shouldBe "0.9KB"
      5000L.pretty              shouldBe "5.0KB"
      1220000L.pretty           shouldBe "1.2MB"
      1234567890L.pretty        shouldBe "1.2GB"
      25894500067890L.pretty    shouldBe "25.9TB"
      18304567800000090L.pretty shouldBe "18.3PB"
      Long.MaxValue.pretty      shouldBe "9.2EB"
    }
    "make Doubles pretty" in {
      (-1D).pretty              shouldBe "-1.0B"
      0.5D.pretty               shouldBe "0.5B"
      0D.pretty                 shouldBe "0.0B"
      899D.pretty               shouldBe "899.0B"
      900D.pretty               shouldBe "0.9KB"
      5000D.pretty              shouldBe "5.0KB"
      1220000D.pretty           shouldBe "1.2MB"
      1234567890D.pretty        shouldBe "1.2GB"
      25894500067890D.pretty    shouldBe "25.9TB"
      18304567800000090D.pretty shouldBe "18.3PB"
      Long.MaxValue.pretty      shouldBe "9.2EB"
    }

  }

}
