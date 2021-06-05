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
      "-Ymacro-annotations",
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
        scalaGuice,
        scalaLogging
      )
        ++ cats
        ++ circe
        ++ pureConfig
    )

  def testDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++=
        (Seq(
          scalaTest,
          scalatic,
          scalaCheck,
          scalaMock
        ) ++
          specs2)
          .map(testDependency)
    )

  val scalaGuice   = "net.codingwell"             %% "scala-guice"   % Versions.scalaGuice
  val playJson     = "com.typesafe.play"          %% "play-json"     % Versions.playJson
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  val enumeratum   = "com.beachape"               %% "enumeratum"    % Versions.enumeratum

  val circe: Seq[ModuleID] = Seq(
    "circe-core",
    "circe-parser",
    "circe-generic",
    "circe-generic-extras",
    "circe-literal",
    "circe-jawn",
    "circe-testing",
    "circe-shapes",
    "circe-refined" // ?
  ).map(artifact => "io.circe" %% artifact % Versions.circe)

  val monix: Seq[ModuleID] = Seq(
    "monix",
    "monix-eval",
    "monix-reactive",
    "monix-execution",
    "monix-tail"
  ).map(artifact => "io.monix" %% artifact % Versions.monix)

  val pureConfig: Seq[ModuleID] = Seq(
    "pureconfig",
    "pureconfig-cats-effect",
    "pureconfig-cats",
    "pureconfig-circe",
    "pureconfig-magnolia"
  ).map(artifact => "com.github.pureconfig" %% artifact % Versions.pureConfig)

  val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"   % Versions.cats,
    "org.typelevel" %% "cats-effect" % Versions.catsEffect,
    "org.typelevel" %% "cats-mtl"    % Versions.catsMtl
  )

  object Test {

    val scalaMock  = "org.scalamock"  %% "scalamock"  % Versions.scalaMock
    val scalaTest  = "org.scalatest"  %% "scalatest"  % Versions.scalaTest
    val scalatic   = "org.scalactic"  %% "scalactic"  % Versions.scalaTest
    val scalaCheck = "org.scalacheck" %% "scalacheck" % Versions.scalaCheck

    val specs2                               = Seq(
      "specs2-mock",
      "specs2-shapeless",
      "specs2-fp",
      "specs2-scalacheck",
      "specs2-tests",
      "specs2-matcher-extra",
      "specs2-matcher",
      "specs2-core",
      "specs2-common",
      "specs2-cats"
    ).map(artifact => "org.specs2" %% artifact % Versions.specs2)

    def testDependency: ModuleID => ModuleID = (module: ModuleID) => module % "test"

  }

  private[Dependencies] object Versions {
    val scalaGuice   = "5.0.1"
    val playJson     = "2.9.2"
    val circe        = "0.14.0"
    val monix        = "3.3.0"
    val cats         = "2.3.0"
    val catsEffect   = "3.1.1"
    val catsMtl      = "1.2.0"
    val pureConfig   = "0.15.0"
    val scalaLogging = "3.9.3"
    val enumeratum   = "1.6.1"
    // Test dependencies
    val scalaMock    = "5.1.0"
    val scalaCheck   = "1.15.4"
    val scalaTest    = "3.2.9"
    val specs2       = "4.12.0"

  }

}
