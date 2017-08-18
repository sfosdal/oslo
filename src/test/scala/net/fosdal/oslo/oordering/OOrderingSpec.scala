package net.fosdal.oslo.oordering

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Ignore, Matchers, WordSpec}

class OOrderingSpec extends WordSpec with Matchers with PropertyChecks {

  "max" must {
    "find the max of a class with an Ordering" in new Fixture {
      max(f1, f2) shouldBe f2
    }
  }

  "min" must {
    "find the max of a class with an Ordering" in new Fixture {
      min(f1, f2) shouldBe f1
    }
  }

  "in" must {
    "determine if the value is in the range, inclusively" in new Fixture {
      f2.in(f1, f3) shouldBe true
      f1.in(f2, f3) shouldBe false
      f1.in(f1, f3) shouldBe true
      f3.in(f1, f3) shouldBe true
    }
  }

  trait Fixture {

    val f1 = Foo(1)
    val f2 = Foo(2)
    val f3 = Foo(3)

    case class Foo(i: Int) extends Ordered[Foo] {
      override def compare(that: Foo): Int = i compare that.i
    }

    implicit val baz: Ordering[Foo] = Ordering.by(_.i)

  }

}
