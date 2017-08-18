sonatypeProfileName := "net.fosdal"

publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("http://fosdal.net/oslo"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sfosdal/oslo"),
    "scm:git@github.com:(account)/(project).git"
  )
)

developers := List(
  Developer(id = "sfosdal", name = "Steve Fosdal", email = "steve@fosdal.net", url = url("http://fosdal.net/"))
)
