name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.12.4"

fork := true

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "joda-time"                  % "joda-time"      % "2.9.9",
  "org.scalacheck"             %% "scalacheck"    % "1.13.5" % Test,
  "org.scalatest"              %% "scalatest"     % "3.0.5" % Test
)

//
// Plugin Settings: sbt-scoverage
//
coverageMinimum := 100
coverageFailOnMinimum := true

//
// Plugin Settings: scalastyle-sbt-plugin
//
scalastyleFailOnError := true

//
// Release Configuration
//
crossScalaVersions := Seq("2.12.4", "2.11.12")
publishMavenStyle := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseProcess += releaseStepCommand("sonatypeRelease")
releaseCrossBuild := true
releaseTagComment := s"$name ${version.value}"
releaseCommitMessage := s"Bump version to ${version.value}"
publishTo := Some(
  if (isSnapshot.value) {
    Opts.resolver.sonatypeSnapshots
  } else {
    Opts.resolver.sonatypeStaging
  }
)

//
// Scalac Flags (selections from: https://tpolecat.github.io/2017/04/25/scalac-flags.html
//
// @formatter:off
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
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

scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings")
