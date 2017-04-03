logLevel := Level.Warn

resolvers += "JBoss" at "https://repository.jboss.org" // TODO see: https://github.com/sbt/sbt-assembly/issues/236

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.6.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "1.0.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")
