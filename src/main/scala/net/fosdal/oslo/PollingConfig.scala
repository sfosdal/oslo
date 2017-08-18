package net.fosdal.oslo

import scala.concurrent.duration.FiniteDuration

case class PollingConfig(initialDelay: FiniteDuration, pollingInterval: FiniteDuration)
