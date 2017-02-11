import sbt._

object Dependencies {

  private[this] val JodaConvertVersion = "1.8.1"
  private[this] val JodaTimeVersion    = "2.9.7"
  private[this] val ScalaCheckVersion  = "1.13.4"
  private[this] val ScalaTestVersion   = "3.0.1"
  private[this] val Slf4jApiVersion    = "1.7.22"

  val JodaConvert = "org.joda"       % "joda-convert" % JodaConvertVersion
  val JodaTime    = "joda-time"      % "joda-time"    % JodaTimeVersion
  val ScalaCheck  = "org.scalacheck" %% "scalacheck"  % ScalaCheckVersion % Test
  val ScalaTest   = "org.scalatest"  %% "scalatest"   % ScalaTestVersion % Test
  val Slf4jApi    = "org.slf4j"      % "slf4j-api"    % Slf4jApiVersion

}
