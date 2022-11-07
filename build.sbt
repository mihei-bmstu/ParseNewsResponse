ThisBuild / version := "0.1.0"

ThisBuild / organization := "ru.digitalleague"

ThisBuild / scalaVersion := "2.12.16"

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

val sparkVersion = "3.1.2"
val circeVersion = "0.14.3"
val core = "org.apache.spark" %% "spark-core" % sparkVersion
val sql = "org.apache.spark" %% "spark-sql" % sparkVersion
val pgDriver = "org.postgresql" % "postgresql" % "42.5.0"
val circeCore = "io.circe" %% "circe-core" % circeVersion
val circeGen = "io.circe" %% "circe-generic" % circeVersion
val circeParser = "io.circe" %% "circe-parser" % circeVersion


lazy val root = (project in file("."))
  .settings(
    name := "ParseNewsResponse",
    assembly / mainClass := Some("Boot"),
    libraryDependencies := Seq(core, sql, pgDriver, circeCore, circeGen, circeParser)
  )
