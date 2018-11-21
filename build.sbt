name := "TCParser"

version := "1.0"

// We're using 2.10 because it's the latest that's compatible with Scalatest 1.x
// Ideally would move to latest Scalatest, but the tests would require refactoring
scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
  // Using latest 1.x version (rather than 2.x), since tests are written using ShouldMatchers
  // and refactoring will be required to move to version 2
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  // scala-reflect included to resolve a version conflict warning from Scalatest
  "org.scala-lang" % "scala-reflect" % "2.10.6",
  // Swing for the GUI
  "org.scala-lang" % "scala-swing" % "2.10.6"
)

// Set main class so that the published .jar can be executable
mainClass in (Compile, packageBin) := Some("tcparsing.TCPFrame")
