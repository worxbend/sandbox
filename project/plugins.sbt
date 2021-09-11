logLevel := util.Level.Debug

addSbtPlugin("org.scalameta"     % "sbt-scalafmt"       % "2.4.3")
addSbtPlugin("com.typesafe.play" % "sbt-plugin"         % "2.8.8")
addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.6.1")
addSbtPlugin("io.gatling"        % "gatling-sbt"        % "3.2.2")
addSbtPlugin("com.typesafe.sbt"  % "sbt-twirl"          % "1.5.1")
addSbtPlugin("ch.epfl.scala"     % "sbt-bloop"          % "1032048a")

addSbtPlugin("com.dwijnand"            % "sbt-dynver"          % "4.1.1")
addSbtPlugin("com.eed3si9n"            % "sbt-buildinfo"       % "0.10.0")
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc"       % "2.0.0")
addSbtPlugin("com.typesafe.sbt"        % "sbt-native-packager" % "1.8.1")
addSbtPlugin("de.heikoseeberger"       % "sbt-header"          % "5.6.0")
