package io.kzonix.sird

object RouteVersioningHelper {
  implicit class RoutePrefixWithVersion(prefix: String) {
    def withVersion(ver: Int = 0): RoutePrefix = {
      RoutePrefix(version = ver, prefix = prefix)
    }
  }
}
