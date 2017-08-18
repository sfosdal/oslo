package net.fosdal.oslo.oordering

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Ignore, Matchers, WordSpec}

class OOrderingSpec extends WordSpec with Matchers with PropertyChecks {

  "max" must {
    "find the max of a class with an Ordering" in new OrderedFixture {
      max(orderedFoo1, orderedFoo2) shouldBe orderedFoo2
    }
  }

  "min" must {
    "find the max of a class with an Ordering" in new OrderedFixture {
      min(orderedFoo1, orderedFoo2) shouldBe orderedFoo1
    }
  }

  "ordered in" must {
    "determine if the value is in the range, inclusively" in new OrderedFixture {
      orderedFoo2.in(orderedFoo1, orderedFoo3) shouldBe true
      orderedFoo1.in(orderedFoo2, orderedFoo3) shouldBe false
      orderedFoo1.in(orderedFoo1, orderedFoo3) shouldBe true
      orderedFoo3.in(orderedFoo1, orderedFoo3) shouldBe true
    }
  }

  "ordering in" must {
    "determine if the value is in the range, inclusively" in new OrderingFixture {
      foo2.in(foo1, foo3) shouldBe true
      foo1.in(foo2, foo3) shouldBe false
      foo1.in(foo1, foo3) shouldBe true
      foo3.in(foo1, foo3) shouldBe true
    }
  }

  trait OrderedFixture {
    val orderedFoo1 = OrderedFoo(1)
    val orderedFoo2 = OrderedFoo(2)
    val orderedFoo3 = OrderedFoo(3)
    case class OrderedFoo(i: Int) extends Ordered[OrderedFoo] {
      override def compare(that: OrderedFoo): Int = i compare that.i
    }
  }

  trait OrderingFixture {
    val foo1 = Foo(1)
    val foo2 = Foo(2)
    val foo3 = Foo(3)
    case class Foo(i: Int)
    implicit val fooOrdering: Ordering[Foo] = Ordering.by(_.i)
  }

}
