package net.fosdal

import net.fosdal.oslo.oduration._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.{implicitConversions, reflectiveCalls}

// TODO use implicit trick for PollingConfig
// TODO create project page
// TODO add CHANGES.md
// TODO integrate with coveralls via circleci
// TODO consider adding isIntable, isDoubleable, isBigDecable
// TODO f.minBy(_.duration).duration
// TODO Map[Option[A],_] => Map[A,_] with defaultValue
// TODO duration shouldBe 2.seconds +- 1.second

// scalastyle:off structural.type
package object oslo {

  implicit def NoOpCloser[A](a: A): Unit = {
    val _ = a
  }

  implicit def CloseCloser[A <: { def close(): Unit }](a: A): Unit = a.close()

  implicit def StopCloser[A <: { def stop(): Unit }](a: A): Unit = a.stop()

  def using[A, B](resource: A)(f: A => B)(implicit closer: A => Unit): B = {
    try f(resource)
    finally closer(resource)
  }

  def time[A](block: => A)(f: (A, FiniteDuration) => Unit): A = {
    val start = System.nanoTime()
    val a     = block
    f(a, (System.nanoTime() - start).nanos)
    a
  }

  def logStatus[A](logger: String => Unit)(block: => A): A = {
    logStatus("processing", logger)(block)
  }

  def logStatus[A](blockName: String, logger: String => Unit)(block: => A): A = {
    logger(s"started $blockName")
    time(block) {
      case (_, duration) => logger(s"completed $blockName (${duration.pretty})")
    }
  }

  def logElapsedTime[Result](logger: (String) => Unit)(block: => Result): Result = {
    time(block) {
      case (_, duration) =>
        logger(s"elapsed time: ${duration.pretty}")
    }
  }

  // change to FiniteDuration
  def sleep(duration: FiniteDuration): Unit = {
    if (duration.toMillis >= 0) {
      Thread.sleep(duration.toMillis)
    } else {
      throw new IllegalArgumentException("duration must be non-negative")
    }
  }

  def sleep(millis: Short): Unit = sleep(millis.milliseconds)
  def sleep(millis: Int): Unit   = sleep(millis.milliseconds)
  def sleep(millis: Long): Unit  = sleep(millis.milliseconds)

  val DefaultPollingConfig: PollingConfig = PollingConfig(1.second, 1.second)

  def pollingUntil(block: => Boolean)(implicit ec: ExecutionContext): Future[Unit] = {
    pollingUntil(DefaultPollingConfig)(block)
  }

  def pollingUntil(pollingConfig: PollingConfig)(block: => Boolean)(implicit ec: ExecutionContext): Future[Unit] = {
    Future {
      sleep(pollingConfig.initialDelay)
      while (!block) sleep(pollingConfig.pollingInterval)
    }
  }

  def pollingUntil(initialDelay: FiniteDuration = 1.second, pollingInterval: FiniteDuration = 1.second)(
      block: => Boolean)(implicit ec: ExecutionContext): Future[Unit] = {
    val pollingConfig = PollingConfig(initialDelay, pollingInterval)
    pollingUntil(pollingConfig)(block)
  }

}
