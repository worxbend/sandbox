import CommonBuildConfiguration._
import Dependencies.{scalaGuice, _}

lazy val commonSettings = {
  commonDependencies ++ BaseSettings.defaultSettings
}

lazy val kzonix = (project in file("."))

lazy val `sird-provider-api` = (project in file(ConfigPaths.Play.api(Seq("sird-provider"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := CommonBuildConfiguration.preformProjectLibraryApiName("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))

lazy val `sird-provider` = (project in file(ConfigPaths.Play.lib(Seq("sird-provider"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := "sird-provider",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`sird-provider-api`)
  .aggregate(`sird-provider-api`)

lazy val `play-utile` = (project in file(ConfigPaths.Play.lib(Seq("play-utile"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := "play-utile",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`sird-provider`)
  .aggregate(`sird-provider`)


lazy val `play-underpressure-api` = (project in file(ConfigPaths.Play.api(Seq("play", "play-underpressure"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := CommonBuildConfiguration.preformProjectLibraryApiName("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))

lazy val `play-underpressure` = (project in file(ConfigPaths.Play.lib(Seq("play", "play-underpressure"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := "play-underpressure",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`play-underpressure-api`)
  .aggregate(`play-underpressure-api`)

lazy val `index-service` = (project in file(ConfigPaths.Play.service(Seq("index"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := preformServiceName("index"),
    libraryDependencies ++= Seq(
      filters,
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`sird-provider`, `play-utile`)
  .aggregate(`sird-provider`, `play-utile`)

// Scalafmt
addCommandAlias("fmt", "; compile:scalafmt; test:scalafmt; scalafmtSbt")
addCommandAlias("fmtCheck", "; compile:scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck")
