import java.text.SimpleDateFormat
import java.util.Calendar

import sbt.Keys._
import sbt.{ Setting, _ }

object BaseSettings {
  val defaultSettings: Seq[Setting[_]] = Seq(
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    scalaVersion := "2.12.8",
    organization := "io.kzonix",
    organizationName := "Kzonix",
    version := Utils.getVersion, // common version number for all services
    homepage := Some(url("https://www.scala-sbt.org")),
    startYear := Some(2018),
    description := "A build tool for Scala.",
    licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      "Akka Snapshot Repository".at("http://repo.akka.io/snapshots/"),
    )
  )

  object Utils {
    def getVersion: String = {
      val date: java.util.Date = Calendar.getInstance.getTime
      new SimpleDateFormat("yy.MM").format(date).concat("-SNAPSHOT")
    }
  }

}

object Dependencies {

  def scalaGuice: ModuleID = Dependencies.scalaGuice

  def commonDependencies: Seq[Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      Dependencies.playJson,
      Dependencies.playJsonJoda
    )
  )

  def testDependencies: Seq[Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      Dependencies.Test.scalaTest,
      Dependencies.Test.scalaMock
    )
  )

  private object Dependencies {

    val scalaGuice = "net.codingwell" %% "scala-guice" % "4.2.2"

    val playJson     = "com.typesafe.play" %% "play-json"      % Versions.playJson
    val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % Versions.playJson

    /* Utils */
    val guava       = "com.google.guava" % "guava"       % "25.0-jre"
    val modelMapper = "org.modelmapper"  % "modelmapper" % "1.1.3"
    val failsafe    = "net.jodah"        % "failsafe"    % "1.1.0"

    /* Security */
    val jjwt     = "io.jsonwebtoken" % "jjwt"         % "0.9.0"
    val imgscalr = "org.imgscalr"    % "imgscalr-lib" % "4.2"

    object Test {
      val scalaMock = "org.scalamock" %% "scalamock" % Versions.scalaMock
      // also add ScalaTest as a framework to run the tests

      val mockito         = "org.mockito"     % "mockito-core"      % "2.18.3"
      val restAssured     = "io.rest-assured" % "rest-assured"      % "3.1.0"
      val assertjCore     = "org.assertj"     % "assertj-core"      % "3.9.1"
      val assertjGuava    = "org.assertj"     % "assertj-guava"     % "3.2.0"
      val assertjJodaTime = "org.assertj"     % "assertj-joda-time" % "2.0.0"
      val scalaTest       = "org.scalatest"   %% "scalatest"        % Versions.scalaTest

    }

    private[Dependencies] object Versions {
      // Play components dependencies
      lazy val playJson = "2.7.0"
      // Joda dependencies
      lazy val jodaConvert         = "2.0.1"
      lazy val jodaTime            = "2.9.9"
      lazy val jodaJacksonDatatype = "2.9.4"
      lazy val jodaMoney           = "0.12"
      lazy val jodaTimeHibernate   = "1.4"
      lazy val jodaBeans           = "2.2"
      lazy val jodaCollect         = "1.0"
      // Test dependencies
      lazy val scalaMock = "4.1.0"
      lazy val scalaTest = "3.0.4"
    }

  }

}
