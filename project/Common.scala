import sbt.Keys._
import sbt.ModuleID
import sbt.Resolver
import sbt.Setting
import sbt.url
import sbt._

import java.text.SimpleDateFormat
import java.util.Calendar

object ProjectNames {

  def service(serviceName: String): String = normalizedName(
    "service",
    serviceName
  )

  def app(serviceName: String): String =
    normalizedName(
      "app",
      serviceName
    )

  def lib(libraryName: String): String =
    normalizedName(
      "impl",
      libraryName
    )

  def api(libraryName: String): String =
    normalizedName(
      "api",
      libraryName
    )

  private def normalizedName(typeName: String = "app", name: String): String =
    s"$name-$typeName"
}

object ConfigPaths {
  // build.sbt relative root directory
  val root = "./"

  def normalizedPath(args: Seq[String]): String = s"${ args.mkString("/") }"

  trait Project {

    private val basePath = "components"

    protected val stack: String

    lazy val lib: Seq[String] => String = (args: Seq[String]) =>
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      )

    lazy val api: Seq[String] => String = (args: Seq[String]) =>
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-api"

    lazy val impl: Seq[String] => String = (args: Seq[String]) =>
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-impl"

    lazy val service: Seq[String] => String = (args: Seq[String]) =>
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-service"

    lazy val app: Seq[String] => String = (args: Seq[String]) =>
      root + normalizedPath(
        List(
          basePath,
          stack
        ) ::: (args toList)
      ) + "-app"

  }

  object Play extends Project {
    override val stack: String = "playframework"
  }

  object Udash extends Project {
    override val stack: String = "udash"
  }

  object ScalaFX extends Project {
    override val stack: String = "scala-fx"
  }

  object VertX extends Project {
    override val stack: String = "vert-x"
  }

  object Http4s extends Project {
    override val stack: String = "http4s"
  }

  object PicoliCLI extends Project {
    override val stack: String = "picoli-cli"
  }

}

object BaseSettings {

  val defaultSettings: Seq[Setting[_]] = Seq(
    versionScheme := Some("semver-spec"),
    scalaVersion := "2.13.6",
    organization := "io.kzonix",
    organizationName := "Kzonix",
    version := Utils.Versions.version(),
    scalaVersion := "2.13.6",
    scalacOptions := Seq(
      "-unchecked",
      "-deprecation",
      "-encoding",
      "utf8"
    ),
    description := "N/A",
    licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.mavenCentral,
      Resolver.sonatypeRepo("snapshots"),
      "Akka Snapshot Repository".at("https://repo.akka.io/snapshots/")
    )
    // ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
    // TODO: Add `publishTo` config for self-hosted sonatype-repo
  )

  object Utils {

    // TODO: Revise version management:
    //  CI pipeline should trigger build with appropriate parameters (snapshot vs release)
    //   - An appropriate environment variable should be provided to set version per CI build.
    //   - Implementation of versioning should be done according to VCS changelog and metadata from sonatype repo (previous version should be provided tp )
    object Versions {

      def version(): String = {
        val date: java.util.Date = Calendar.getInstance.getTime
        new SimpleDateFormat("yy.MM.dd.HHmmssSSS").format(date)
      }

      def milestone(num: Int): String =
        version().concat(s"M$num")

      def generalAvailability: String =
        version().concat("-GA")

      def beta(num: Int): String =
        version().concat("-%04db".format(num))
    }

  }

}

object Dependencies {
  import Test._

  def commonDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        playJson,
        scalaGuice
      )
    )

  def testDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        testDependency(scalaTest),
        testDependency(scalaMock)
      )
    )

  val scalaGuice = "net.codingwell"    %% "scala-guice" % "5.0.0"
  val playJson   = "com.typesafe.play" %% "play-json"   % Versions.playJson

  object Test {

    val scalaMock = "org.scalamock" %% "scalamock" % Versions.scalaMock
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest

    def testDependency: ModuleID => ModuleID = (module: ModuleID) => module % "test"

  }

  private[Dependencies] object Versions {
    // Play components dependencies
    lazy val playJson  = "2.7.4"
    // Test dependencies
    lazy val scalaMock = "5.1.0"
    lazy val scalaTest = "3.1.0"
    lazy val scalactic = "3.0.8"
  }

}
