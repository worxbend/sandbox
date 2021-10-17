import sbt.Keys._
import sbt.ModuleID
import sbt.Resolver
import sbt.Setting
import sbt.url
import sbt._

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.language.postfixOps

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

object ProjectPaths {
  // build.sbt relative root directory
  val root = "./"

  def normalizedPath(args: Seq[String]): String = s"${ args.mkString("/") }"

  trait ProjectType {
    protected val basePath: String

  }

  trait GeneralComponent extends Project {
    override val basePath: String = "components"
  }

  trait Application extends Project {
    override val basePath: String = "applications"
  }

  trait Project extends ProjectType {

    protected val projectMainPath: String

    def api(args: Seq[String]): String =
      root + normalizedPath(
        List(
          basePath,
          projectMainPath
        ) ::: (args toList)
      ) + "-api"

    def impl(args: Seq[String]): String =
      root + normalizedPath(
        List(
          basePath,
          projectMainPath
        ) ::: (args toList)
      ) + "-impl"

    def lib(args: Seq[String]): String =
      root + normalizedPath(
        List(
          basePath,
          projectMainPath
        ) ::: (args toList)
      )

    def service(args: Seq[String]): String =
      root + normalizedPath(
        List(
          basePath,
          projectMainPath
        ) ::: (args toList)
      ) + "-service"

    def app(args: Seq[String]): String =
      root + normalizedPath(
        List(
          basePath,
          projectMainPath
        ) ::: (args toList)
      ) + "-app"

  }

  object Components {

    object Common extends GeneralComponent {
      override val projectMainPath: String = "common"
    }

    object Play extends GeneralComponent {
      override val projectMainPath: String = "playframework"
    }

    object Akka extends GeneralComponent {
      override val projectMainPath: String = "akka"
    }

    object Udash extends GeneralComponent {
      override val projectMainPath: String = "udash"
    }

    object ScalaFX extends GeneralComponent {
      override val projectMainPath: String = "scala-fx"
    }

    object VertX extends GeneralComponent {
      override val projectMainPath: String = "vert-x"
    }

    object Http4s extends GeneralComponent {
      override val projectMainPath: String = "http4s"
    }

    object PicoliCLI extends GeneralComponent {
      override val projectMainPath: String = "picoli-cli"
    }

  }

  object Applications {

    object Common extends Application {
      override val projectMainPath: String = "common"
    }

    object Sandbox extends Application {
      override val projectMainPath: String = "sandbox"
    }

    object Root extends Application {
      override val projectMainPath: String = "./"
    }

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
    startYear := Some(2020),
    scalaVersion := "2.13.6",
    organization := "io.kzonix",
    organizationName := "Kzonix Projects",
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
    licenses += ("MIT", url("https://www.gnu.org/licenses/gpl-2.0.html")),
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.mavenCentral,
      Resolver.sonatypeRepo("snapshots"),
      "Akka Snapshot Repository".at("https://repo.akka.io/snapshots/")
    )
    // ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
    // TODO: Add `publishTo` config for self-hosted sonatype-repo
  )

  val scala3: Seq[Setting[_]] = defaultSettings ++ Seq[Setting[_]](
    scalaVersion := "3.0.0"
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
  import Versions._

  def commonDependencies: Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        playJson,
        scalaGuice,
        scalaLogging
      )
        ++ monix
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

  val playJson = "com.typesafe.play" %% "play-json" % PlayJson

  val scalaGuice     = "net.codingwell"             %% "scala-guice"       % ScalaGuice
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging"     % ScalaLogging
  val enumeratum     = "com.beachape"               %% "enumeratum"        % Enumeratum
  val alpakkaKafka   = "com.typesafe.akka"          %% "akka-stream-kafka" % AlpakkaKafka
  val kafkaClients   = "org.apache.kafka"            % "kafka-clients"     % KafkaClients
  val logbackLogging = "ch.qos.logback"              % "logback-classic"   % Logback

  val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-stream"                % Akka,
    "com.typesafe.akka" %% "akka-actor-typed"           % Akka,
    "com.typesafe.akka" %% "akka-slf4j"                 % Akka,
    "com.typesafe.akka" %% "akka-serialization-jackson" % Akka
  )

  val config = Seq(
    "com.typesafe" % "config" % TypesafeConfig
  )

  val akkaTest: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % Akka,
    "com.typesafe.akka" %% "akka-stream-testkit"      % Akka
  ).map(testDependency)

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
  ).map(artifact => "io.circe" %% artifact % Circe)

  val monix: Seq[ModuleID] = Seq(
    "monix-eval",
    "monix-reactive",
    "monix-execution",
    "monix-tail"
  ).map(artifact => "io.monix" %% artifact % Monix)

  val pureConfig: Seq[ModuleID] = Seq(
    "pureconfig",
    "pureconfig-cats",
    "pureconfig-circe"
  ).map(artifact => "com.github.pureconfig" %% artifact % PureConfig)

  val micrometerPrometheus: Seq[ModuleID] = Seq(
    "io.micrometer" % "micrometer-registry-prometheus" % MicrometerPrometheus,
    "io.micrometer" % "micrometer-core"                % MicrometerPrometheus
  )

  val atomix: Seq[ModuleID] = Seq(
    "io.atomix" % "atomix"                % Atomix,
    "io.atomix" % "atomix-raft"           % Atomix,
    "io.atomix" % "atomix-primary-backup" % Atomix,
    "io.atomix" % "atomix-gossip"         % Atomix
  )

  val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"   % Cats,
    "org.typelevel" %% "cats-effect" % CatsEffect,
    "org.typelevel" %% "cats-mtl"    % CatsMtl
  )

  object Test {

    val scalaMock  = "org.scalamock"  %% "scalamock"  % ScalaMock
    val scalaTest  = "org.scalatest"  %% "scalatest"  % ScalaTest
    val scalatic   = "org.scalactic"  %% "scalactic"  % ScalaTest
    val scalaCheck = "org.scalacheck" %% "scalacheck" % ScalaCheck

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
    ).map(artifact => "org.specs2" %% artifact % Specs2)

    def testDependency: ModuleID => ModuleID = (module: ModuleID) => module % "test"

  }

  private[Dependencies] object Versions {
    val TypesafeConfig       = "1.4.1"
    val ScalaGuice           = "5.0.1"
    val PlayJson             = "2.9.2"
    val Circe                = "0.14.0"
    val Monix                = "3.4.0"
    val Cats                 = "2.6.1"
    val CatsEffect           = "3.2.1"
    val CatsMtl              = "1.2.1"
    val PureConfig           = "0.15.0"
    val ScalaLogging         = "3.9.4"
    val Enumeratum           = "1.7.0"
    // Test dependencies
    val ScalaMock            = "5.1.0"
    val ScalaCheck           = "1.15.4"
    val ScalaTest            = "3.2.9"
    val Specs2               = "4.12.0"
    val Akka                 = "2.6.15"
    val Jackson              = "2.12.4"
    val AlpakkaKafka         = "2.1.1"
    val KafkaClients         = "2.7.0"
    val Logback              = "1.2.5"
    val MicrometerPrometheus = "1.7.4"
    val Atomix               = "3.1.10"
  }

}
