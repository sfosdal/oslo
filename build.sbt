import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.11.11"

fork := true

crossScalaVersions := Seq("2.12.2", "2.11.12")

publishArtifact in Test := false

publishTo := Some(
  if (isSnapshot.value) {
    Opts.resolver.sonatypeSnapshots
  } else {
    Opts.resolver.sonatypeStaging
  }
)

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
  pushChanges
)

scalastyleFailOnError := true

(scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml"

libraryDependencies ++= Seq(
  "com.opencsv"                %  "opencsv"       % "4.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "joda-time"                  %  "joda-time"     % "2.9.9",
  "org.scalacheck"             %% "scalacheck"    % "1.13.5" % Test,
  "org.scalatest"              %% "scalatest"     % "3.0.4"  % Test
)

dependencyUpdatesFilter -= moduleFilter(organization = "org.scala-lang")

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

coverageMinimum := 100
coverageFailOnMinimum := true
