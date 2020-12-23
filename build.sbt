name := "mgs"

version := "0.1"

scalaVersion := "2.12.12"
val neo4jVersion = "4.1.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
libraryDependencies += "org.neo4j.driver" % "neo4j-java-driver" % "4.2.0"
libraryDependencies += "org.neo4j" % "neo4j" % neo4jVersion
libraryDependencies += "org.neo4j.test" % "neo4j-harness" % neo4jVersion % Test
libraryDependencies += "org.neo4j" % "neo4j-ogm-core" % "3.2.18"
libraryDependencies += "org.neo4j" % "neo4j-ogm-bolt-driver" % "3.2.18"
libraryDependencies += "junit" % "junit" % "4.13.1"
libraryDependencies += "org.assertj" % "assertj-core" % "2.4.1" % Test
