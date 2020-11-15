import java.text.SimpleDateFormat
import java.util.Calendar

import sbt._
import sbt.Keys._

object BaseSettings {
  val defaultSettings: Seq[Setting[_]] = Seq(
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    scalaVersion := "2.13.3",
    organization := "io.kzonix",
    organizationName := "Kzonix",
    version := Utils.Versions.snapshot(1), // common version number for all services
    homepage := Some(url("http://recursive-escalator.io")),
    startYear := Some(2018),
    description := "N/A",
    licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      "Akka Snapshot Repository".at("https://repo.akka.io/snapshots/")
    )
  )

  object Utils {

    object Versions {
      def snapshot: String = {
        version.concat("-SNAPSHOT")
      }

      private def version = {
        val date: java.util.Date = Calendar.getInstance.getTime
        new SimpleDateFormat("yy.MM").format(date)
      }

      def snapshot(subVersion: Int): String = {
        version.concat("-%02d".format(subVersion)).concat("-SNAPSHOT")
      }

      def milestone(num: Int): String = {
        version.concat(s"M$num")
      }

      def generalAvailability: String = {
        version.concat("-GA")
      }

      def beta(num: Int): String = {
        version.concat("-%04db".format(num))
      }
    }
  }

}

object Dependencies {

  def scalaGuice: ModuleID = Dependencies.scalaGuice

  def commonDependencies: Seq[Setting[_]] =
    Seq(libraryDependencies ++= Seq(Dependencies.playJson, Dependencies.playJsonJoda))

  def testDependencies: Seq[Setting[_]] =
    Seq(libraryDependencies ++= Seq(Dependencies.Test.scalaTest, Dependencies.Test.scalaMock))

  private object Dependencies {

    val scalaGuice = "net.codingwell" %% "scala-guice" % "4.2.11"

    val playJson     = "com.typesafe.play" %% "play-json"      % Versions.playJson
    val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % Versions.playJson

    /* Utils */
    val scalactic   = "org.scalactic"   %% "scalactic"   % Versions.scalactic
    val guava       = "com.google.guava" % "guava"       % "30.0-jre"
    val modelMapper = "org.modelmapper"  % "modelmapper" % "2.3.8"
    val failsafe    = "net.jodah"        % "failsafe"    % "2.4.0"

    /* Security */
    val jjwt     = "io.jsonwebtoken" % "jjwt"         % "0.9.1"
    val imgscalr = "org.imgscalr"    % "imgscalr-lib" % "4.2"

    object Test {

      val scalaMock                          = test("org.scalamock" % "scalamock_2.13.0-M3" % Versions.scalaMock)
      val mockito                            = test("org.mockito" % "mockito-core" % "2.24.5")
      val restAssured                        = test("io.rest-assured" % "rest-assured" % "3.3.0")
      val assertjCore                        = test("org.assertj" % "assertj-core" % "3.12.0")
      val assertjGuava                       = test("org.assertj" % "assertj-guava" % "3.2.1")
      val scalaTest                          = test("org.scalatest" %% "scalatest" % Versions.scalaTest)
      private def test: ModuleID => ModuleID = (d: ModuleID) => { d % "test" }

    }

    private[Dependencies] object Versions {
      // Play components dependencies
      lazy val playJson  = "2.7.4"
      // Test dependencies
      lazy val scalaMock = "4.2.0"
      lazy val scalaTest = "3.0.8"
      lazy val scalactic = "3.0.8"
    }

  }

}
