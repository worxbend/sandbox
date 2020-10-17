package io.kzonix.sird

case class RoutePrefix(version: Int, prefix: String) {
  def isVersional: Boolean = this.version > 0
}
