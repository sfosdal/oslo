logLevel := Level.Warn

addSbtPlugin("com.github.gseitz"                 % "sbt-release"            % "1.0.5")
addSbtPlugin("com.jsuereth"                      % "sbt-pgp"                % "1.1.0")
addSbtPlugin("com.lucidchart"                    % "sbt-scalafmt"           % "1.12")
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings"       % "1.1.0")
addSbtPlugin("com.timushev.sbt"                  % "sbt-updates"            % "0.3.3")
addSbtPlugin("com.typesafe.sbt"                  % "sbt-git"                % "0.9.3")
addSbtPlugin("net.virtual-void"                  % "sbt-dependency-graph"   % "0.9.0")
addSbtPlugin("org.scalastyle"                    %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scoverage"                     % "sbt-coveralls"          % "1.2.2")
addSbtPlugin("org.scoverage"                     % "sbt-scoverage"          % "1.5.1")
addSbtPlugin("org.xerial.sbt"                    % "sbt-sonatype"           % "2.0")

// see: https://github.com/sbt/sbt-git#known-issues
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"
