name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.12.2"

fork := true

coverageMinimum := 10

coverageFailOnMinimum := true

coverageEnabled := true

publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/Desktop/oslo-release")))

crossScalaVersions := Seq("2.12.2", "2.11.11")

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
