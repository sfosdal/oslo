package net.fosdal.oslo

import scala.concurrent.duration.FiniteDuration

case class UntilConfig(initialDelay: FiniteDuration, delay: FiniteDuration)
