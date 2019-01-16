import Dependencies.{scalaGuice, _}

lazy val commonSettings = {
  commonDependencies ++ BaseSettings.defaultSettings
}

// TODO: override project configuration rules with 'inThisBuild' instead of custom implementation
lazy val kzonix = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "org.projectlombok" % "lombok" % "1.16.16"
    )
  )

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

