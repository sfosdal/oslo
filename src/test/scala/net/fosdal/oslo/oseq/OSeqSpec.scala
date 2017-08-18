package net.fosdal.oslo.oseq

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Ignore, Matchers, WordSpec}

class OSeqSpec extends WordSpec with Matchers with PropertyChecks {

  "minOption and maxOption" when {

    "given arbitrary non-empty lists of Integers" must {
      "return the equivalent result as min and max" in {
        forAll(nonEmptyListOf[Int](arbInt.arbitrary)) { s: Seq[Int] =>
          s.minOption shouldBe Some(s.min)
          s.maxOption shouldBe Some(s.max)
        }
      }
      "use a provided reverse ordering and return the opposite result as min and max" in new Fixture {
        forAll(nonEmptyListOf[Int](arbInt.arbitrary)) { s: Seq[Int] =>
          s.minOption(reversed) shouldBe Some(s.max)
          s.maxOption(reversed) shouldBe Some(s.min)
        }
      }
    }

    "given arbitrary empty lists of Integers" must {
      "return None" in new Fixture {
        Seq().minOption shouldBe None
        Seq().maxOption shouldBe None
      }
    }

    "given a list of things that have an ordering" must {
      "return the min and max correctly" in new Fixture {
        Seq(Bar(1), Baz(2)).minOption shouldBe Some(Bar(1))
        Seq(Bar(1), Baz(2)).maxOption shouldBe Some(Baz(2))
      }

    }
  }

  "minOptionBy and maxOptionBy" when {
    "given a sequence of strings" must {
      "find the min/max with the supplied function" in {
        Seq("1", "2", "3").minOptionBy(s => -s.toInt) shouldBe Some("3")
        Seq("1", "2", "3").maxOptionBy(s => -s.toInt) shouldBe Some("1")
      }
    }
    "given an empty sequence of strings" must {
      "find the min/max with the supplied function" in {
        Seq.empty[String].minOptionBy(s => -s.toInt) shouldBe None
        Seq.empty[String].maxOptionBy(s => -s.toInt) shouldBe None
      }
    }
  }

  trait Fixture {

    val reversed = implicitly[Ordering[Int]].reverse

    sealed trait Foo { def i: Int }
    case class Bar(i: Int) extends Foo
    case class Baz(i: Int) extends Foo

    implicit def ordering[F <: Foo]: Ordering[F] = new Ordering[F] {
      override def compare(f1: F, f2: F): Int = f1.i compare f2.i
    }

  }

}
