import com.typesafe.sbt.packager.Keys.maintainer
import sbt.Keys._
import sbt._

import java.text.SimpleDateFormat
import java.util.Calendar

object BaseSettings {

  val defaultSettings: Seq[Setting[_]] = Seq(
    scalacOptions := Seq(
      "-unchecked",
      "-deprecation",
      "-encoding",
      "utf8"
    ),
    scalaVersion := "2.13.4",
    organization := "io.kzonix",
    organizationName := "Kzonix",
    maintainer := "balyszyn@gmail.com",
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
      def snapshot: String =
        version.concat("-SNAPSHOT")

      private def version = {
        val date: java.util.Date = Calendar.getInstance.getTime
        new SimpleDateFormat("yy.MM").format(date)
      }

      def snapshot(subVersion: Int): String =
        version + "-%02d".format(subVersion) + "-SNAPSHOT"

      def milestone(num: Int): String =
        version.concat(s"M$num")

      def generalAvailability: String =
        version.concat("-GA")

      def beta(num: Int): String =
        version.concat("-%04db".format(num))
    }

  }

}

object Dependencies {

  def scalaGuice: ModuleID = Dependencies.scalaGuice

  def commonDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        Dependencies.playJson,
        Dependencies.playJsonJoda
      )
    )

  def testDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        Dependencies.Test.scalaTest,
        Dependencies.Test.scalaMock
      )
    )

  private object Dependencies {

    val scalaGuice = "net.codingwell" %% "scala-guice" % "4.2.11"

    val playJson     = "com.typesafe.play" %% "play-json"      % Versions.playJson
    val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % Versions.playJson

    /* Utils */
    val scalactic = "org.scalactic"   %% "scalactic" % Versions.scalactic
    val guava     = "com.google.guava" % "guava"     % "30.1-jre"
    val failsafe  = "net.jodah"        % "failsafe"  % "2.4.0"

    /* Security */
    val jjwt     = "io.jsonwebtoken" % "jjwt"         % "0.9.1"
    val imgscalr = "org.imgscalr"    % "imgscalr-lib" % "4.2"

    object Test {

      val scalaMock    = "org.scalamock"   % "scalamock_2.13.0-M3" % Versions.scalaMock
      val mockito      = "org.mockito"     % "mockito-core"        % "2.28.2"
      val restAssured  = "io.rest-assured" % "rest-assured"        % "3.3.0"
      val assertjCore  = "org.assertj"     % "assertj-core"        % "3.19.0"
      val assertjGuava = "org.assertj"     % "assertj-guava"       % "3.4.0"
      val scalaTest    = "org.scalatest"  %% "scalatest"           % Versions.scalaTest

      def testDependency: ModuleID => ModuleID = (module: ModuleID) => {
        module % "test"
      }

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
