package net.fosdal.oslo.oany

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class OAnySpec extends WordSpec with Matchers with PropertyChecks {

  "tap" must {
    "operate on the supplied instance and return it when complete" in new Fixture {
      val touchable = Touchable("unused")
      touchable should not be 'touched
      var didIt = false
      val tappedTouchable: Touchable = touchable tap { (t: Touchable) =>
        didIt = true
        t shouldBe theSameInstanceAs(touchable)
        t.touch()
        t shouldBe 'touched
      }
      didIt           shouldBe true
      tappedTouchable shouldBe theSameInstanceAs(touchable)
      touchable       shouldBe 'touched
    }
  }

  "partialTap" must {
    "operate on the supplied instance only if it is covered by the partial function and then return it when complete" in new Fixture {
      val touchable = Touchable("covered")
      touchable should not be 'touched
      var didIt = false
      val tappedTouchable: Touchable = touchable partialTap {
        case (t: Touchable) if t.id == "covered" =>
          didIt = true
          t shouldBe theSameInstanceAs(touchable)
          t.touch()
          t shouldBe 'touched
      }
      didIt           shouldBe true
      tappedTouchable shouldBe theSameInstanceAs(touchable)
      touchable       shouldBe 'touched
    }
    "not operate on the supplied instance if it is not covered by the partial function and then return it when complete" in new Fixture {
      val touchable = Touchable("not covered")
      touchable should not be 'touched
      var didIt = false
      val tappedTouchable: Touchable = touchable partialTap {
        case (t: Touchable) if t.id == "covered" =>
          didIt = true
          t shouldBe theSameInstanceAs(touchable)
          t.touch()
          t shouldBe 'touched
      }
      didIt           shouldBe false
      tappedTouchable shouldBe theSameInstanceAs(touchable)
      touchable should not be 'touched
    }
  }

  trait Fixture {
    case class Touchable(id: String) {
      var isTouched = false
      def touch(): Unit = isTouched = true
      def reset(): Unit = isTouched = false
    }
  }

}
