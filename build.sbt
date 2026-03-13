val scala3Version = "3.3.7"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ToSAB",
    version := "0.1.0",
    scalaVersion := scala3Version,

    // Testing library
    libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.3" % Test,

    // --- sbt-assembly settings ---
    // Naming convention for the final output JAR (e.g., ToSAB-assembly-0.1.0.jar)
    assembly / assemblyJarName := s"${name.value}-assembly-${version.value}.jar",

    // The exact path of the application entry point
    assembly / mainClass := Some("it.unibo.tosab.Main")
  )