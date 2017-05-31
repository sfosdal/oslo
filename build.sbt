name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.12.2"

fork := true

coverageMinimum := 0

coverageFailOnMinimum := true

publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/Desktop/oslo-release")))

crossScalaVersions := Seq("2.12.2", "2.11.8")

enablePlugins(BuildInfoPlugin)

// TODO update dependencies
libraryDependencies ++= Seq(
  "com.datadoghq"              % "java-dogstatsd-client"        % "2.3",
  "com.github.melrief"         %% "pureconfig"                  % "0.7.0",
  "com.google.cloud"           % "google-cloud-bigquery"        % "0.18.0-beta",
  "com.opencsv"                % "opencsv"                      % "3.9",
  "com.typesafe"               % "config"                       % "1.3.1",
  "com.typesafe.scala-logging" %% "scala-logging"               % "3.5.0",
  "joda-time"                  % "joda-time"                    % "2.9.9",
  "org.apache.logging.log4j"   % "log4j-api"                    % "2.8.2",
  "org.apache.logging.log4j"   % "log4j-core"                   % "2.8.2",
  "org.apache.logging.log4j"   % "log4j-slf4j-impl"             % "2.8.2",
  "org.joda"                   % "joda-convert"                 % "1.8.1",
  "org.scalacheck"             %% "scalacheck"                  % "1.13.5" % Test,
  "org.scalamock"              %% "scalamock-scalatest-support" % "3.6.0" % Test,
  "org.scalatest"              %% "scalatest"                   % "3.0.3" % Test,
  "org.slf4j"                  % "slf4j-api"                    % "1.7.25"
)

// format: off
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-value-discard"
)
