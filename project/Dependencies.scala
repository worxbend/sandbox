import sbt.Keys._
import sbt.ModuleID
import sbt.Setting
import sbt._

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
