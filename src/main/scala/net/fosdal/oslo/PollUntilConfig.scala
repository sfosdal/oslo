package net.fosdal.oslo

import scala.concurrent.duration.FiniteDuration

case class PollUntilConfig(initialDelay: FiniteDuration, pollingInterval: FiniteDuration)
