import BaseSettings.Utils
import BaseSettings.defaultSettings
import Dependencies.scalaGuice
import Dependencies._
import sbt.Test

/* -- START: Global Settings -- */
Global / onChangedBuildSource := ReloadOnSourceChanges
Global / homepage := Some(url("http://recursive-escalator.io"))
Global / startYear := Some(2018)

/* -- END:   Global Settings  -- */

lazy val commonSettings = {
  commonDependencies ++ testDependencies ++ defaultSettings
}

lazy val kzonix = (project in file("."))
  .settings(defaultSettings: _*)
  .settings(
    name := "kzonix"
  )
  .aggregate(
    `twitee-service`,
    `index-service`,
    `redprime-service`
  )

lazy val `sird-provider-api` = (project in file(ConfigPaths.Play.api(Seq("sird-provider"))))
  .enablePlugins(PlayService)
  .settings(defaultSettings: _*)
  .settings(
    name := ProjectNames.api("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )

lazy val `sird-provider` = (project in file(ConfigPaths.Play.lib(Seq("sird-provider"))))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    version := Utils.Versions.version(),
    name := ProjectNames.lib("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(`sird-provider-api`)
  .aggregate(`sird-provider-api`)

lazy val `play-utile` = (project in file(ConfigPaths.Play.lib(Seq("play-utile"))))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.lib("play-utile"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(`sird-provider`)
  .aggregate(`sird-provider`)

lazy val `play-underpressure-api` = (project in file(
  ConfigPaths.Play.api(
    Seq(
      "play",
      "play-underpressure"
    )
  )
))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.api("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )

lazy val `play-underpressure` = (project in file(
  ConfigPaths.Play.lib(
    Seq(
      "play",
      "play-underpressure"
    )
  )
))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.lib("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(`play-underpressure-api`)
  .aggregate(`play-underpressure-api`)

/* ------- Applications ------ */

lazy val `redprime-service` = (project in file(ConfigPaths.Play.service(Seq("redprime"))))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.service("redprime"),
    libraryDependencies ++= Seq(
      filters,
      caffeine,
      guice,
      scalaGuice,
      logback,
      ws
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(
    `sird-provider`,
    `play-utile`
  )
  .aggregate(
    `sird-provider`,
    `play-utile`
  )

lazy val `index-service` = (project in file(ConfigPaths.Play.service(Seq("index"))))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.service("index"),
    libraryDependencies ++= Seq(
      filters,
      caffeine,
      guice,
      scalaGuice,
      logback,
      "com.azure" % "azure-storage-blob" % "12.12.0"
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(
    `sird-provider`,
    `play-utile`
  )
  .aggregate(
    `sird-provider`,
    `play-utile`
  )

lazy val `twitee-service` = (project in file(ConfigPaths.Play.service(Seq("twitee"))))
  .enablePlugins(PlayService)
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.service("redprime"),
    libraryDependencies ++= Seq(
      filters,
      caffeine,
      guice,
      scalaGuice,
      logback,
      ws
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.JUnit,
        "-a",
        "-v"
      )
    )
  )
  .dependsOn(
    `sird-provider`,
    `play-utile`
  )
  .aggregate(
    `sird-provider`,
    `play-utile`
  )

lazy val `cogwheel` = (project in file(ConfigPaths.Play.lib(Seq("cogwheel"))))
  .settings(commonSettings: _*)
  .settings(
    name := ProjectNames.service("cogwheel"),
    Compile / run / mainClass := Some("io.kzonix.cogwheel.Main"),
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk-ssm" % "1.12.28"
    ),
    Test / testOptions := Seq(
      Tests.Argument(
        TestFrameworks.ScalaTest,
        "-oD"
      )
    )
  )

// Scalafmt
addCommandAlias(
  "fmt",
  "; compile:scalafmt; test:scalafmt; scalafmtSbt"
)
addCommandAlias(
  "fmtCheck",
  "; compile:scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck"
)
