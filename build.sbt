import BaseSettings.defaultSettings
import CommonBuildConfiguration._
import Dependencies.scalaGuice
import Dependencies._

lazy val commonSettings = {
  commonDependencies ++ testDependencies ++ defaultSettings
}

lazy val kzonix = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "kzonix"
  )

lazy val `sird-provider-api` = (project in file(ConfigPaths.Play.api(Seq("sird-provider"))))
  .enablePlugins(PlayService)
  .settings(defaultSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformProjectLibraryApiName("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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
    name := CommonBuildConfiguration.preformProjectLibraryImplName("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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
    name := "play-utile",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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
    name := preformProjectLibraryApiName("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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
    name := preformProjectLibraryImplName("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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
    name := preformServiceName("redprime"),
    libraryDependencies ++= Seq(
      filters,
      caffeine,
      guice,
      scalaGuice,
      logback,
      ws
    ),
    testOptions in Test := Seq(
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
    name := preformServiceName("index"),
    libraryDependencies ++= Seq(
      filters,
      caffeine,
      guice,
      scalaGuice,
      logback
    ),
    testOptions in Test := Seq(
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

// Scalafmt
addCommandAlias(
  "fmt",
  "; compile:scalafmt; test:scalafmt; scalafmtSbt"
)
addCommandAlias(
  "fmtCheck",
  "; compile:scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck"
)
