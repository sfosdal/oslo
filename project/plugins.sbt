logLevel := Level.Warn

addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.6.8")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.5")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.1")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "1.0.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

// TODO periodically check for resolved sbt-git issue:
// see: https://github.com/sbt/sbt-git#known-issues
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"

// TODO integrate with https://github.com/scoverage/sbt-coveralls
// addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")
