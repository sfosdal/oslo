package net.fosdal.oslo

import net.fosdal.oslo.util.OsloMatchers
import org.scalacheck.Gen.alphaNumStr
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.reflectiveCalls

class OsloSpec extends WordSpec with Matchers with PropertyChecks with OsloMatchers {

  "using" must {

    "ignore null Closeables" in new UsingFixture {
      val closable: UsingClosable = null // scalastyle:ignore null
      using(closable) { c =>
        c shouldBe theSameInstanceAs(closable)
      }
    }

    "close the Closable being used" in new UsingFixture {
      val closable = new UsingClosable
      closable should not be 'closed
      using(closable) { c =>
        c shouldBe theSameInstanceAs(closable)
      }
      closable shouldBe 'closed
      an[UnsupportedOperationException] should be thrownBy {
        using(closable) { c =>
          c shouldBe theSameInstanceAs(closable)
        }
      }
    }

    "ignore null Stoppables" in new UsingFixture {
      val stoppable: UsingStoppable = null // scalastyle:ignore null
      using(stoppable) { c =>
        c shouldBe theSameInstanceAs(stoppable)
      }
    }

    "stop the Stoppable being used" in new UsingFixture {
      val stoppable = new UsingStoppable
      stoppable should not be 'stopped
      stoppable.isStopped shouldBe false
      using(stoppable) { s =>
        s shouldBe theSameInstanceAs(stoppable)
      }
      stoppable shouldBe 'stopped
      an[UnsupportedOperationException] should be thrownBy {
        using(stoppable) { s =>
          s shouldBe theSameInstanceAs(stoppable)
        }
      }
    }

    "ignore null Shutdownables" in new UsingFixture {
      val shutdownable: UsingShutdownable = null // scalastyle:ignore null
      using(shutdownable) { c =>
        c shouldBe theSameInstanceAs(shutdownable)
      }
    }

    "shutdown the Shutdownable being used" in new UsingFixture {
      val shutdownable = new UsingShutdownable
      shutdownable should not be 'shutdown
      shutdownable.isShutdown shouldBe false
      using(shutdownable) { s =>
        s shouldBe theSameInstanceAs(shutdownable)
      }
      shutdownable shouldBe 'shutdown
      an[UnsupportedOperationException] should be thrownBy {
        using(shutdownable) { s =>
          s shouldBe theSameInstanceAs(shutdownable)
        }
      }
    }

    "do do nothing with the NoOpable being used" in new UsingFixture {
      val noOpable = new UsingNoOpable
      using(noOpable) { _ =>
        ()
      }
      using(noOpable) { _ =>
        ()
      }
    }

  }

  "time" must {
    "must time things" in {
      time({
        sleep(2.seconds)
        "result"
      }) {
        case (result, duration) =>
          result shouldBe "result"
          duration.toMillis shouldBe 2.seconds.toMillis +- 100
          ()
      }
    }
  }

  "logStatus" must {

    "use the block name, logger, and block" in new LoggerFixture {
      forAll(alphaNumStr, alphaNumStr) { (blockName: String, block: String) =>
        logger.reset()
        logger.logged shouldBe 'empty
        val result = logStatus(blockName, logger)(block)
        result shouldBe block
        val expected = Seq(s"started $blockName", s"""completed $blockName (.+)""")
        logger.logged.length shouldBe expected.length
        logger.logged.zip(expected).foreach {
          case (act, exp) =>
            (act should fullyMatch).regex(exp)
        }
      }
    }

    "use the default block name, logger, and block" in new LoggerFixture {
      forAll() { block: String =>
        logger.reset()
        logger.logged shouldBe 'empty
        val result = logStatus(logger)(block)
        result shouldBe block
        val expected = Seq(s"started processing", s"""completed processing (.+)""")
        logger.logged.length shouldBe expected.length
        logger.logged.zip(expected).foreach {
          case (act, exp) =>
            (act should fullyMatch).regex(exp)
        }
      }
    }

  }

  "logElapsedTime" must {
    "use the logger and block" in new LoggerFixture {
      forAll() { (block: String) =>
        logger.reset()
        logger.logged shouldBe 'empty
        val result = logElapsedTime(logger)(block)
        result shouldBe block
        val expected = Seq(s"""elapsed time: .+""")
        logger.logged.length shouldBe expected.length
        logger.logged.zip(expected).foreach {
          case (act, exp) =>
            (act should fullyMatch).regex(exp)
        }
      }
    }
  }

  "sleep" must {

    "throw exceptions on negative durations" in {
      (the[IllegalArgumentException] thrownBy {
        sleep(-1)
      } should have).message("duration must be non-negative")
    }

    "use Short millis to sleep" in {
      val millis = 2000.toShort
      val start = System.nanoTime()
      sleep(millis)
      val actual = (System.nanoTime() - start).nanos.toMillis
      actual.toShort shouldBe 2000.toShort +- 100.toShort
    }

    "use Int millis to sleep" in {
      val millis = 2000
      val start = System.nanoTime()
      sleep(millis)
      val actual = (System.nanoTime() - start).nanos.toMillis
      actual.toInt shouldBe 2000 +- 100
    }

    "use Long millis to sleep" in {
      val millis = 2000L
      val start = System.nanoTime()
      sleep(millis)
      val actual = (System.nanoTime() - start).nanos.toMillis
      actual shouldBe 2000L +- 100L
    }

  }

  "pollingUntil" must {

    "periodically poll its block until the condition is met" in new UntilFixture {
      val limit = 3
      val mutable = new UntilMutable(limit)

      mutable.i shouldBe 0
      val myFuture: Future[Unit] = pollUntil {
        mutable.check()
      }
      mutable.i shouldBe 0
      Await.ready(myFuture, (limit + 1).seconds)
      mutable.i shouldBe limit
    }

    "be configurable with config class" in new UntilFixture {
      val limit = 3
      val mutable = new UntilMutable(limit)

      mutable.i shouldBe 0

      val config = PollUntilConfig(initialDelay = 0.seconds, pollingInterval = 1.second)

      val myFuture: Future[Unit] = pollUntil(config) {
        mutable.check()
      }
      sleep(1.second)
      mutable.i should be > 0
      Await.ready(myFuture, (limit + 1).seconds)
      mutable.i shouldBe limit
    }

    "be configurable with plain values" in new UntilFixture {
      val limit = 3
      val mutable = new UntilMutable(limit)

      mutable.i shouldBe 0

      val myFuture: Future[Unit] = pollUntil(initialDelay = 0.seconds, pollingInterval = 1.second) {
        mutable.check()
      }
      sleep(1.second)
      mutable.i should be > 0
      Await.ready(myFuture, (limit + 1).seconds)
      mutable.i shouldBe limit
    }

  }

  "tap" when {

    "as an explicit method" must {
      "operate on the supplied instance and return it when complete" in new TapFixture {
        val touchable = Touchable("unused")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = tap(touchable) { t =>
          didIt = true
          t shouldBe theSameInstanceAs(touchable)
          t.touch()
          t shouldBe 'touched
          ()
        }
        didIt shouldBe true
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable shouldBe 'touched
      }
    }

    "as an implicit method" must {
      "operate on the supplied instance and return it when complete" in new TapFixture {
        val touchable = Touchable("unused")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = touchable.tap { t =>
          didIt = true
          t shouldBe theSameInstanceAs(touchable)
          t.touch()
          t shouldBe 'touched
          ()
        }
        didIt shouldBe true
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable shouldBe 'touched
      }
    }

  }

  "partialTap" must {

    "as an implicit method" must {

      "operate on the supplied instance only if it is covered by the partial function and then return it when complete" in new TapFixture {
        val touchable = Touchable("covered")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = touchable.partialTap {
          case t if t.id == "covered" =>
            didIt = true
            t shouldBe theSameInstanceAs(touchable)
            t.touch()
            t shouldBe 'touched
            ()
        }
        didIt shouldBe true
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable shouldBe 'touched
      }

      "not operate on the supplied instance if it is not covered by the partial function and then return it when complete" in new TapFixture {
        val touchable = Touchable("not covered")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = touchable.partialTap {
          case t if t.id == "covered" =>
            didIt = true
            t shouldBe theSameInstanceAs(touchable)
            t.touch()
            t shouldBe 'touched
            ()
        }
        didIt shouldBe false
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable should not be 'touched
      }

    }

    "as an explicit method" must {

      "operate on the supplied instance only if it is covered by the partial function and then return it when complete" in new TapFixture {
        val touchable = Touchable("covered")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = partialTap(touchable) {
          case t if t.id == "covered" =>
            didIt = true
            t shouldBe theSameInstanceAs(touchable)
            t.touch()
            t shouldBe 'touched
            ()
        }
        didIt shouldBe true
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable shouldBe 'touched
      }

      "not operate on the supplied instance if it is not covered by the partial function and then return it when complete" in new TapFixture {
        val touchable = Touchable("not covered")
        touchable should not be 'touched
        var didIt = false
        val tappedTouchable: Touchable = partialTap(touchable) {
          case t if t.id == "covered" =>
            didIt = true
            t shouldBe theSameInstanceAs(touchable)
            t.touch()
            t shouldBe 'touched
            ()
        }
        didIt shouldBe false
        tappedTouchable shouldBe theSameInstanceAs(touchable)
        touchable should not be 'touched
      }

    }

  }

  trait TapFixture {

    case class Touchable(id: String) {
      var isTouched = false

      def touch(): Unit = isTouched = true

      def reset(): Unit = isTouched = false
    }

  }

  trait UsingFixture {

    class UsingNoOpable

    class UsingClosable {
      var isClosed = false

      def close(): Unit = {
        if (!isClosed) {
          isClosed = true
        } else {
          throw new UnsupportedOperationException
        }
      }
    }

    class UsingStoppable {
      var isStopped = false

      def stop(): Unit = {
        if (!isStopped) {
          isStopped = true
        } else {
          throw new UnsupportedOperationException
        }
      }
    }

    class UsingShutdownable {
      var isShutdown = false

      def shutdown(): Unit = {
        if (!isShutdown) {
          isShutdown = true
        } else {
          throw new UnsupportedOperationException
        }
      }
    }

  }

  trait UntilFixture {

    class UntilMutable(limit: Int) {
      var i: Int = 0

      def check(): Boolean = {
        i = i + 1
        i >= limit
      }
    }

  }

  trait LoggerFixture {
    lazy val logger = new ((String) => Unit) {
      var logged = Seq.empty[String]

      def reset(): Unit = logged = Nil

      override def apply(s: String): Unit = logged = logged :+ s
    }
  }

}
