import Dependencies.{scalaGuice, _}
import CommonBuildConfiguration._

lazy val commonSettings = {
  commonDependencies ++ BaseSettings.defaultSettings
}

lazy val kzonix = (project in file("."))

lazy val `sird-provider-api` = (project in file(ConfigPaths.api(Seq("sird-provider"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := CommonBuildConfiguration.preformProjectLibraryApiName("sird-provider"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))

lazy val `sird-provider` = (project in file(ConfigPaths.lib(Seq("sird-provider"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := "sird-provider",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`sird-provider-api`)
  .aggregate(`sird-provider-api`)


lazy val `play-underpressure-api` = (project in file(ConfigPaths.api(Seq("play","play-underpressure"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := CommonBuildConfiguration.preformProjectLibraryApiName("play-underpressure"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))

lazy val `play-underpressure` = (project in file(ConfigPaths.lib(Seq("play","play-underpressure"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := "play-underpressure",
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`play-underpressure-api`)
  .aggregate(`play-underpressure-api`)

lazy val `index-service` = (project in file(ConfigPaths.service(Seq("index"))))
  .enablePlugins(PlayService).settings(commonSettings: _*)
  .settings(inThisBuild(Seq(
    name := preformServiceName("index"),
    libraryDependencies ++= Seq(
      guice,
      scalaGuice,
      logback,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )))
  .dependsOn(`sird-provider`)
  .aggregate(`sird-provider`)

