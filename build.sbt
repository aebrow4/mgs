name := "mgs"

version := "0.1"

scalaVersion := "2.13.6"
val neo4jVersion = "4.1.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
libraryDependencies += "org.neo4j.driver" % "neo4j-java-driver" % "4.2.0"
libraryDependencies += "org.neo4j" % "neo4j" % neo4jVersion
libraryDependencies += "io.github.neotypes" %% "neotypes-core" % "0.18.2"
libraryDependencies += "org.scala-lang" % "scala-library" % "2.13.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
libraryDependencies += "org.scalatest" %% "scalatest-funspec" % "3.2.9" % "test"
