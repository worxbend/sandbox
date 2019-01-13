import java.text.SimpleDateFormat
import java.util.Calendar

import sbt.Keys._
import sbt.{Setting, _}

object BaseSettings {
  val defaultSettings: Seq[Setting[_]] = Seq(
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    scalaVersion := "2.12.7",
    organization := "io.limpid.kzonix",
    organizationName := "Kzonix",
    version := Util.getVersion, // common version number for all services
    homepage := Some(url("https://www.scala-sbt.org")),
    startYear := Some(2018),
    description := "A build tool for Scala.",
    licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
    resolvers ++= Seq(
      "rediscala" at "http://dl.bintray.com/etaty/maven",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
      "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
      "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
    )
  )

  object Util {
    def getVersion: String = {
      val date: java.util.Date = Calendar.getInstance.getTime
      new SimpleDateFormat("yy.MM").format(date).concat("_SNAPSHOT")
    }
  }

}

object Dependencies {

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

  def jodaDependencies: Seq[Serializable] = Seq(
    Dependencies.jodaTime,
    Dependencies.jodaBeans,
    Dependencies.jodaCollect,
    Dependencies.jodaMoney,
    Dependencies.jodaJacksonDatatype,
    Dependencies.jodaConvert
  )

  private object Dependencies {

    val playJson = "com.typesafe.play" %% "play-json" % Versions.playJson
    val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % Versions.playJson
    val jodaConvert = "org.joda" % "joda-convert" % Versions.jodaConvert
    val jodaTime = "joda-time" % "joda-time" % Versions.jodaTime
    val jodaTimeHibernate = "joda-time" % "joda-time-hibernate" % Versions.jodaTimeHibernate
    val jodaJacksonDatatype = "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % Versions.jodaJacksonDatatype
    val jodaMoney = "org.joda" % "joda-money" % Versions.jodaMoney
    val jodaBeans = "org.joda" % "joda-beans" % Versions.jodaBeans
    val jodaCollect = "org.joda" % "joda-collect" % Versions.jodaCollect

    /* Utils */
    val lombok = "org.projectlombok" % "lombok" % "1.16.16"
    val guava = "com.google.guava" % "guava" % "25.0-jre"
    val modelMapper = "org.modelmapper" % "modelmapper" % "1.1.3"
    val orika = "ma.glasnost.orika" % "orika-core" % "1.5.2"
    val failsafe = "net.jodah" % "failsafe" % "1.1.0"

    /* Security */
    val jjwt = "io.jsonwebtoken" % "jjwt" % "0.9.0"
    val imgscalr = "org.imgscalr" % "imgscalr-lib" % "4.2"
    
    /* Functional extension for Java */
    val vavr = "io.vavr" % "vavr" % "0.9.2"
    val vavrJackson = "io.vavr" % "vavr-jackson" % "0.9.2"
    val cyclops = "com.oath.cyclops" % "cyclops" % "10.0.0-M7"
    val cyclopsVavr = "com.oath.cyclops" % "cyclops-vavr-integration" % "10.0.0-M7"
    val cyclopsJackson = "com.oath.cyclops" % "cyclops-jackson-integration" % "10.0.0-M7"
    val fugue = "io.atlassian.fugue" % "fugue" % "4.6.1"

    object Test {
      val scalaMock = "org.scalamock" %% "scalamock" % Versions.scalaMock
      // also add ScalaTest as a framework to run the tests

      val mockito = "org.mockito" % "mockito-core" % "2.18.3"
      val restAssured = "io.rest-assured" % "rest-assured" % "3.1.0"
      val assertjCore = "org.assertj" % "assertj-core" % "3.9.1"
      val assertjGuava = "org.assertj" % "assertj-guava" % "3.2.0"
      val assertjJodaTime = "org.assertj" % "assertj-joda-time" % "2.0.0"
      val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest

    }

    private[Dependencies] object Versions {
      // Play components dependencies
      lazy val playJson = "2.7.0-RC2"
      // Joda dependencies
      lazy val jodaConvert = "2.0.1"
      lazy val jodaTime = "2.9.9"
      lazy val jodaJacksonDatatype = "2.9.4"
      lazy val jodaMoney = "0.12"
      lazy val jodaTimeHibernate = "1.4"
      lazy val jodaBeans = "2.2"
      lazy val jodaCollect = "1.0"
      // Hibernate/JPA dependencies
      lazy val hibernate = "6.0.8.Final"
      // Test dependencies
      lazy val scalaMock = "4.1.0"
      lazy val scalaTest = "3.0.4"
    }

  }


}
