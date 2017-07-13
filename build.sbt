import ReleaseTransformations._

name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.11.11"

fork := true

crossScalaVersions := Seq("2.12.2", "2.11.11")

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
  "com.opencsv"                % "opencsv"        % "3.9",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "joda-time"                  % "joda-time"      % "2.9.9",
  "org.scalatest"              %% "scalatest"     % "3.0.3" % Test
)

// format: off
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
