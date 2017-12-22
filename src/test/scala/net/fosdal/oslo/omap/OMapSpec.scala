package net.fosdal.oslo.omap

import org.scalatest.{Matchers, WordSpec}

class OMapSpec extends WordSpec with Matchers {

  "toProperties" when {

    "given an empty map" must {
      "make an empty Property obj" in {
        val p = Map.empty[String, String].toProperties
        p shouldBe 'empty
      }
    }

    "given an map" must {
      "make the equivalent Property obj" in {
        val p = Map("k1" -> "v1", "k2" -> "v2").toProperties
        p.size shouldBe 2
        p.getProperty("k1") shouldBe "v1"
        p.getProperty("k2") shouldBe "v2"
      }
    }

  }

}
