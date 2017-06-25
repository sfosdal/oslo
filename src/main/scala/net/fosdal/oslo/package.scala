package net.fosdal

import net.fosdal.oslo.oany._
import net.fosdal.oslo.oduration._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.{implicitConversions, reflectiveCalls}

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

  def logStatus[A](logger: String => Unit)(block: => A): A = logStatus("processing", logger)(block)

  def logStatus[A](blockName: String, logger: String => Unit)(block: => A): A = {
    logger(s"started $blockName")
    time(block) {
      case (_, duration) => logger(s"completed $blockName (${duration.pretty})")
    }
  }

  def logElapsedTime[Result](logger: (String) => Unit)(block: => Result): Result = {
    val start = System.nanoTime()
    block tap { (_: Result) =>
      val delta = (System.nanoTime() - start).nanoseconds
      logger(s"elapsed time: ${delta.pretty}")
    }
  }

  def sleep(duration: FiniteDuration): Unit = sleep(duration.toMillis)

  def sleep(millis: Long): Unit = Thread.sleep(millis)

  implicit val DefaultConfiguration = UntilConfig(20.seconds, 5.seconds)

  def until(block: => Boolean)(implicit config: UntilConfig, ec: ExecutionContext): Future[Unit] = {
    sleep(config.initialDelay)
    Future {
      while (!block) {
        sleep(config.delay)
      }
    }
  }

  def until(config: UntilConfig)(block: => Boolean)(implicit ec: ExecutionContext): Future[Unit] = {
    until(block)(config, ec)
  }

}
