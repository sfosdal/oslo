import Dependencies._

name := "oslo"

organization := "net.fosdal"

scalaVersion := "2.12.1"

fork := true

coverageMinimum := 0

coverageFailOnMinimum := true

publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/Desktop/oslo-release")))

crossScalaVersions := Seq("2.12.1", "2.11.8")

enablePlugins(BuildInfoPlugin)

libraryDependencies ++= Seq(Slf4jApi, JodaConvert, JodaTime, ScalaTest, ScalaCheck)

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
