lazy val compilerOptions = Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings"
)

lazy val commonSettings = Seq(
  name := """grp1b-pizza""",
  version := "Milestone3",
  scalaVersion := "2.11.11",
  libraryDependencies ++= Seq(
    jdbc,
    anorm,
    cache,
    ws,
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
  ),
  scalacOptions ++= compilerOptions,
  resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"
)

lazy val testDependencies = Seq(
    "org.specs2" %% "specs2-scalacheck" % "3.5" % "test",
    "org.specs2" %% "specs2-junit" % "3.5" % "test",
    "org.specs2" %% "specs2-mock" % "3.5" % "test"
)

lazy val root = project.in(file("."))
  .settings(moduleName := "grp1b-pizza")
  .settings(commonSettings)
  .settings(libraryDependencies ++= testDependencies)
  // .settings(coverageMinimum := 80)
  .settings(coverageExcludedPackages := "app\\.forms\\..*;app\\.models\\..*;app\\.rest\\..*;conf\\..*;public\\..*;test\\..*")
  .enablePlugins(PlayScala)