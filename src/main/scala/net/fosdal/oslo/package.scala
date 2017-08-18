package net.fosdal

import net.fosdal.oslo.PollUntilConfig
import net.fosdal.oslo.oduration._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.{implicitConversions, reflectiveCalls}

// scalastyle:off structural.type
package object oslo extends Oslo {

  implicit def NoOpCloser[A](a: A): Unit = {
    val _ = a
  }

  implicit def CloseCloser[A <: { def close(): Unit }](a: A): Unit = a.close()
  implicit def StopCloser[A <: { def stop(): Unit }](a: A): Unit   = a.stop()

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

  def sleep(millis: Short): Unit = sleep(millis.toInt.milliseconds)

  def sleep(millis: Int): Unit = sleep(millis.milliseconds)

  def sleep(millis: Long): Unit = sleep(millis.milliseconds)

  def pollUntil(block: => Boolean)(implicit config: PollUntilConfig, ec: ExecutionContext): Future[Unit] = {
    pollUntil(config)(block)
  }

  def pollUntil(config: PollUntilConfig)(block: => Boolean)(implicit ec: ExecutionContext): Future[Unit] = {
    Future {
      sleep(config.initialDelay)
      while (!block) sleep(config.pollingInterval)
    }
  }

  def pollUntil(initialDelay: FiniteDuration = 1.second, pollingInterval: FiniteDuration = 1.second)(block: => Boolean)(
      implicit ec: ExecutionContext): Future[Unit] = {
    val config = PollUntilConfig(initialDelay, pollingInterval)
    pollUntil(config)(block)
  }

}

trait Oslo {
  implicit val DefaultPollUntilConfig: PollUntilConfig = PollUntilConfig(1.second, 1.second)
}
