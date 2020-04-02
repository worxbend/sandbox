import sbt.util

logLevel := util.Level.Debug
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"       % "1.5.1")
addSbtPlugin("com.typesafe.play" % "sbt-plugin"         % "2.8.1")
addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.5.1")
addSbtPlugin("io.gatling"        % "gatling-sbt"        % "3.1.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-twirl"          % "1.5.0")
