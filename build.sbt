/*
* Aggregated configuration with all default settings for modules
* */
lazy val commonSettings =
  Dependencies.commonDependencies ++ BaseSettings.defaultSettings

// TODO: override project configuration rules with 'inThisBuild' instead of custom implementation
lazy val kzonix = (project in file("."))
  .aggregate(`jongo-module`, `kundera-mongo-module`)
  .settings(
    libraryDependencies ++= Seq(
      "org.projectlombok" % "lombok" % "1.16.16"
    )
  )

lazy val `kzonix-oauth-api` = (project in file(ConfigPaths.api(Seq("oauth", "kzonix-oauth"))))
  .enablePlugins(PlayJava).settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("oauth"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `kzonix-oauth-impl` = (project in file(ConfigPaths.impl(Seq("oauth", "kzonix-oauth"))))
  .enablePlugins(PlayJava).settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformImplLibraryName("oauth"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  ).aggregate(`kzonix-oauth-api`)

lazy val `kzonix-oauth-service` = (project in file(ConfigPaths.service(Seq("oauth", "kzonix-oauth"))))
  .enablePlugins(PlayJava).settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformServiceName("oauth"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  ).aggregate(`kzonix-oauth-api`)


lazy val `kzonix-http-api` = (project in file(ConfigPaths.api(Seq("http-client", "kzonix-http"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("http"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `kzonix-http-impl` = (project in file(ConfigPaths.impl(Seq("http-client", "kzonix-http"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformImplLibraryName("http"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `http-client-commons` = (project in file(ConfigPaths.api(Seq("http-client", "commons"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("commons"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `kzonix-mailer` = (project in file(ConfigPaths.api(Seq("common", "mail-module", "kzonix-mailer"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("mailer"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `kzonix-socail` = (project in file(ConfigPaths.api(Seq("common", "social-module", "kzonix-social"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("social"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `jwt-api` = (project in file(ConfigPaths.api(Seq("common", "jwt-module", "kzonix-jwt"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("jwt"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `jwt-core-api` = (project in file(ConfigPaths.api(Seq("common", "jwt-module", "kzonix-jwt-core"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("jwt-core"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `jwt-mail-api` = (project in file(ConfigPaths.api(Seq("common", "jwt-module", "kzonix-jwt-mail"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("jwt-mail"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

lazy val `dashboard` = (project in file(ConfigPaths.api(Seq("app-component", "monitoring", "kzonix-dashboard"))))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("dashboard"),
    libraryDependencies ++= Seq(
      cacheApi,
      ws,
      guice,
      "com.github.karelcemus" %% "play-redis" % "2.0.2",
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    ),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )


lazy val `error-handler-core` = (project in file(ConfigPaths.api(Seq("common", "error-handler", "core-error-handler"))))
  .enablePlugins(PlayJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("core-error-handler"),
    libraryDependencies ++= Seq(
      cacheApi,
      ws,
      guice,
      "com.github.karelcemus" %% "play-redis" % "2.0.2",
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    )
  )

lazy val `social-service` = (project in file(ConfigPaths.api(Seq("social", "user-social"))))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformServiceName("user-social"),
    libraryDependencies ++= Seq(
      cacheApi,
      ws,
      guice,
      "com.github.karelcemus" %% "play-redis" % "2.0.2",
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    )
  )

lazy val `messaging-service` = (project in file(ConfigPaths.service(Seq("messaging", "user-messaging"))))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformServiceName("user-messaging"),
    libraryDependencies ++= Seq(
      cacheApi,
      ws,
      guice,
      "com.github.karelcemus" %% "play-redis" % "2.0.2",
      "com.h2database" % "h2" % "1.4.196",
      "org.assertj" % "assertj-core" % "3.6.2" % Test,
      "org.awaitility" % "awaitility" % "2.0.0" % Test
    )
  )

lazy val `kundera-mongo-module` = (project in file(ConfigPaths.api(Seq("common", "mongo", "kundera-mongo"))))
  .enablePlugins(PlayMinimalJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("morphia"),
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "com.impetus.kundera.client" % "kundera-mongo" % "3.12",
      "org.mongodb" % "mongo-java-driver" % "3.7.0",
      "org.easytesting" % "fest-assert" % "1.4" % "test"
    )
  )

lazy val `jongo-module` = (project in file(ConfigPaths.api(Seq("common", "mongo", "jongo"))))
  .enablePlugins(PlayMinimalJava)
  .settings(commonSettings: _*)
  .settings(
    name := CommonBuildConfiguration.preformApiLibraryName("jongo"),
    libraryDependencies ++= Seq(
      cacheApi,
      ws,
      guice,
      "org.jongo" % "jongo" % "1.4.0",
      "org.mongodb" % "mongo-java-driver" % "3.7.0"
    )
  )

