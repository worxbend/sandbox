package io.kzonix.sird

case class RoutePrefix(version: Int, prefix: String) {
  def isVersional: Boolean = version > 0
}
