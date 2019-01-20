package io.kzonix.play

package object underpressure {

  object HealthStatus extends Enumeration {
    type HealthStatus = Value
    val OK, NOT_OK, IDLE, WARNING = Value
  }

  trait HealthInfo {
  }
}
