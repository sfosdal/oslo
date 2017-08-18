package net.fosdal.oslo.oany

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

class OAnySpec extends WordSpec with Matchers with PropertyChecks {

  "in" must {
    "determine if the value is in the range, inclusively" in new Fixture {
      f2.in(f1, f3) shouldBe true
      f1.in(f2, f3) shouldBe false
      f1.in(f1, f3) shouldBe true
      f3.in(f1, f3) shouldBe true
    }
  }

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
    val f1 = Foo(1)
    val f2 = Foo(2)
    val f3 = Foo(3)

    case class Foo(i: Int)

    implicit val baz: Ordering[Foo] = Ordering.by(_.i)

    case class Touchable(id: String) {
      var isTouched = false

      def touch(): Unit = isTouched = true

      def reset(): Unit = isTouched = false
    }

  }

}
