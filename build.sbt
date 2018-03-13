name := "kzonix"

Common.settings

lazy val `kzonix` = (project in file(".")).enablePlugins(PlayScala)

lazy val `kzonix-oauth-api` = (project in file("./kzonix_oauth_api"))

lazy val `kzonix-master-data-api` = (project in file("./kzonix_master_data_api"))

lazy val `kzonix-master-messaging-api` = (project in file("./kzonix_master_messaging_api"))

lazy val `kzonix-master-tools` = (project in file("./kzonix_master_tools"))

lazy val `kzonix-core-api` = (project in file("./kzonix_core_api"))

lazy val `kzonix-anorm-core` = (project in file("./kzonix_anorm_core"))

lazy val `kzonix-rss-integration` = (project in file("./kzonix_rss_integration"))

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases")

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      