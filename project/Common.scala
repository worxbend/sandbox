import sbt._
import Keys._

object Common {
  def settings: Seq[Setting[_]] = Seq(
    organization := "com.kzonix.axis",
    version := "1.0"
  )
}
