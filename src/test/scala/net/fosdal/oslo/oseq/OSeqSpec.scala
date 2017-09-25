package net.fosdal.oslo.oseq

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

// scalastyle:off magic.number
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
      "return None" in {
        Seq.empty[String].minOptionBy(s => -s.toInt) shouldBe None
        Seq.empty[String].maxOptionBy(s => -s.toInt) shouldBe None
      }
    }
  }

  "minByAndApply and maxByAndApply" when {
    "given a sequence of classes" must {
      "find the min/max with the supplied function and return the value after the function is applied" in new Fixture {
        Seq(Bar(3), Bar(2), Bar(7)).minByAndApply(_.i) shouldBe 2
        Seq(Bar(5), Bar(3), Bar(9)).maxByAndApply(_.i) shouldBe 9
      }
    }
  }

  "minOptionByAndApply and maxOptionByAndApply" when {
    "given a sequence of classes" must {
      "find the min/max with the supplied function and return the value after the function is applied" in new Fixture {
        Seq(Bar(3), Bar(2), Bar(7)).minOptionByAndApply(_.i) shouldBe Some(2)
        Seq(Bar(5), Bar(3), Bar(9)).maxOptionByAndApply(_.i) shouldBe Some(9)
      }
    }
    "given an empty sequence of classes" must {
      "return None" in new Fixture {
        Seq.empty[Foo].minOptionByAndApply(_.i) shouldBe None
        Seq.empty[Foo].maxOptionByAndApply(_.i) shouldBe None
      }
    }
  }
  "minByAndApplyOrElse and maxByAndApplyOrElse" when {
    "given a sequence of classes" must {
      "find the min/max with the supplied function and return the value after the function is applied" in new Fixture {
        Seq(Bar(3), Bar(2), Bar(7)).minByAndApplyOrElse(_.i, 101) shouldBe 2
        Seq(Bar(5), Bar(3), Bar(9)).maxByAndApplyOrElse(_.i, 102) shouldBe 9
      }
    }
    "given an empty sequence of classes" must {
      "return the default" in new Fixture {
        Seq.empty[Foo].minByAndApplyOrElse(_.i, 101) shouldBe 101
        Seq.empty[Foo].maxByAndApplyOrElse(_.i, 102) shouldBe 102
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
