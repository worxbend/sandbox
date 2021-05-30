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

  private val warningOptions: Seq[String] = Seq(
    "-Wdead-code",        // Warn when dead code is identified.
    "-Wextra-implicit",   // Warn when more than one implicit parameter section is defined.
    "-Wnumeric-widen",    // Warn when numerics are widened.
    "-Woctal-literal",    // Warn on obsolete octal syntax.
    "-Wunused:imports",   // Warn if an import selector is not referenced.
    "-Wunused:patvars",   // Warn if a variable bound in a pattern is unused.
    "-Wunused:privates",  // Warn if a private member is unused.
    "-Wunused:locals",    // Warn if a local definition is unused.
    "-Wunused:explicits", // Warn if an explicit parameter is unused.
    "-Wunused:implicits", // Warn if an implicit parameter is unused.
    "-Wunused:params",    // Enable -Wunused:explicits,implicits.
    "-Wunused:linted",    // -Xlint:unused.
    "-Wvalue-discard"     // Warn when non-Unit expression results are unused.
  )

  private val lintOptions: Seq[String] = Seq(
    "-Xlint:adapted-args",
    "-Xlint:nullary-unit",           // Warn when nullary methods return Unit.
    "-Xlint:inaccessible",           // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",              // Warn when a type argument is inferred to be Any.
    "-Xlint:missing-interpolator",   // A string literal appears to be missing an interpolator id.
    "-Xlint:doc-detached",           // A Scaladoc comment appears to be detached from its element.
    "-Xlint:private-shadow",         // A private field (or class parameter) shadows a superclass field.
    "-Xlint:type-parameter-shadow",  // A local type parameter shadows a type already in scope.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:option-implicit",        // Option.apply used implicit view.
    "-Xlint:delayedinit-select",     // Selecting member of DelayedInit.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:stars-align",            // Pattern sequence wildcard must align with sequence component.
    "-Xlint:constant",               // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:unused",                 // Enable -Ywarn-unused:imports,privates,locals,implicits.
    "-Xlint:nonlocal-return",        // A return statement used an exception for flow control.
    "-Xlint:implicit-not-found",     // Check @implicitNotFound and @implicitAmbiguous messages.
    "-Xlint:serial",                 // @SerialVersionUID on traits and non-serializable classes.
    "-Xlint:valpattern",             // Enable pattern checks in val definitions.
    "-Xlint:eta-zero",               // Warn on eta-expansion (rather than auto-application) of zero-ary method.
    "-Xlint:eta-sam",                // Warn on eta-expansion to meet a Java-defined functional interface that is not explicitly annotated with @FunctionalInterface.
    "-Xlint:deprecation",            // Enable linted deprecations.
    "-Xlint:implicit-recursion"      // Warn when an implicit resolves to an enclosing self-definition.
  )

  val defaultSettings: Seq[Setting[_]] = Seq(
    versionScheme := Some("semver-spec"),
    scalaVersion := "2.13.6",
    organization := "io.kzonix",
    organizationName := "Kzonix",
    version := Utils.Versions.version(),
    scalaVersion := "2.13.6",
    scalacOptions := Seq[String](
      "-unchecked",
      //"-print",
      "-deprecation",
      "-feature",
      "-encoding",
      "utf8",         // scala 3 non-compatible
      "-Werror",      // scala 3 non-compatible
      "-explaintypes" // scala 3 non-compatible
    ) ++ warningOptions ++ lintOptions,
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

  val scalaGuice = "net.codingwell"    %% "scala-guice" % Versions.scalaGuice
  val playJson   = "com.typesafe.play" %% "play-json"   % Versions.playJson

  object Test {

    val scalaMock = "org.scalamock" %% "scalamock" % Versions.scalaMock
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest

    def testDependency: ModuleID => ModuleID = (module: ModuleID) => module % "test"

  }

  private[Dependencies] object Versions {
    // Play components dependencies
    lazy val scalaGuice   = "5.0.1"
    lazy val playJson     = "2.9.2"
    lazy val circe        = "0.14.1"
    lazy val monix        = "3.4.0"
    lazy val cats         = "2.0.0"
    lazy val pureConfig   = "0.11.1"
    lazy val scalaLogging = "3.9.2"
    lazy val shapeless    = "2.3.3"
    // Test dependencies
    lazy val scalaMock    = "5.1.0"
    lazy val scalaTest    = "3.1.0"

  }

}
