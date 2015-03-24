/*
   Copyright 2013 Ilya Lakhin (Илья Александрович Лахин)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
import sbt._
import Keys._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object PapaCarlo extends Build {
  val papaCarloVersion = "0.7.0"
  
  val baseSettings = Seq(
    name := "Papa Carlo",
    version := papaCarloVersion,

    scalacOptions += "-unchecked",

    description :=
      "Constructor of incremental parsers in Scala using PEG grammars",
    homepage := Some(new URL("http://lakhin.com/projects/papa-carlo/")),

    organization := "name.lakhin.eliah.projects.papacarlo",
    organizationHomepage  := Some(new URL("http://lakhin.com/")),

    licenses := Seq("The Apache Software License, Version 2.0" ->
      new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    startYear := Some(2013),

    scalaVersion := "2.11.6",

    crossScalaVersions := Seq("2.10.4", "2.11.6")
  )

  lazy val PapaCarlo: sbt.Project = Project(
    id = "root",
    base = file(".")
  ).aggregate(jvm, `js-demo`)

  lazy val jvm = project.in(file("./jvm/")).
    settings(baseSettings: _*).
    settings(
      unmanagedSourceDirectories in Compile +=
        baseDirectory.value.getParentFile / "src" / "main" / "scala",

      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.0",
        "net.liftweb" %% "lift-json" % "2.6-M4"
      ),
      resolvers ++= Seq(
        "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
        "typesafe" at "http://repo.typesafe.com/typesafe/releases/"
      ),

      testOptions in Test += Tests.Argument("-oD"),

      publishMavenStyle := true,
      pomIncludeRepository := { _ => false },
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishArtifact in Test := false,
      publishTo <<= version {
        version =>
          val nexus = "https://oss.sonatype.org/"
          if (version.endsWith("SNAPSHOT"))
            Some("snapshots" at nexus + "content/repositories/snapshots")
          else
            Some("releases" at nexus + "service/local/staging/deploy/maven2")
      },
      pomExtra :=
        <scm>
          <url>git@github.com:Eliah-Lakhin/papa-carlo.git</url>
          <connection>scm:git:git@github.com:Eliah-Lakhin/papa-carlo.git</connection>
        </scm>
        <developers>
          <developer>
            <id>Eliah-Lakhin</id>
            <name>Ilya Lakhin</name>
            <email>eliah.lakhin@gmail.com</email>
            <url>http://lakhin.com/</url>
          </developer>
        </developers>
    )

  lazy val `js-demo` = project.in(file("./js/demo/")).
    enablePlugins(ScalaJSPlugin).
    settings(baseSettings: _*).
    settings(
      //libraryDependencies += "org.scala-lang.modules.scalajs" %%
      //  "scalajs-jasmine-test-framework" % scalaJSVersion % "test",

      unmanagedSourceDirectories in Compile +=
        baseDirectory.value.getParentFile.getParentFile / "src" / "main" / "scala",

      excludeFilter in unmanagedSources := "test"
    )
}
