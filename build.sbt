
name := "TCParser"

scalaVersion := "2.9.0-1"

mainClass in (Compile, packageBin) := Some("tcparsing.TCPFrame")


libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.0-1"
