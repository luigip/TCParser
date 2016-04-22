
name := "TCParser"

scalaVersion := "2.10.6"

// set main class so that the published .jar can be executable
mainClass in (Compile, packageBin) := Some("tcparsing.TCPFrame")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
//  "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
  "org.scala-lang" % "scala-reflect" % "2.10.6",
  "org.scala-lang" % "scala-swing" % "2.10.6"
)

