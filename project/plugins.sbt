import sbt.util

logLevel := util.Level.Debug
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"       % "1.5.1")
addSbtPlugin("com.typesafe.play" % "sbt-plugin"         % "2.8.0-M6")
addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.4.4")
addSbtPlugin("io.gatling"        % "gatling-sbt"        % "3.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-twirl"          % "1.4.2")
