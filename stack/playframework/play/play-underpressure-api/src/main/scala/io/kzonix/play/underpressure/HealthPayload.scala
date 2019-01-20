package io.kzonix.play.underpressure

import io.kzonix.play.underpressure.HealthStatus._

case class HealthPayload[A <: HealthInfo](status: HealthStatus, providedInfo: Option[A] = None)

