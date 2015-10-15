enablePlugins(ScalaJSPlugin)
scalaVersion := "2.11.6"
version := "0.1-SNAPSHOT"
name := "BaasBoxAPI"
resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-language:implicitConversions",
  "-language:reflectiveCalls"
)
persistLauncher in Compile := false
persistLauncher in Test := false
skip in packageJSDependencies := false
libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "scalarx" % "0.2.8",
  "org.monifu" %%% "monifu" % "1.0-M1"
)

libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "utest" % "0.3.0" % "test"
)

postLinkJSEnv := PhantomJSEnv().value

libraryDependencies += "biz.cgta" %%%! "otest-sjs" % "0.2.1" % "test"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"

//libraryDependencies += "org.scala-js" % "scalajs-tools_2.11" % "0.6.4"

jsDependencies += ProvidedJS / "baasbox.js"

jsDependencies += ProvidedJS / "jquery-1.9.1.min.js"

libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.8.1"

//select https://github.com/cgta/otest for the testing
//testFrameworks := Seq(new TestFramework("cgta.otest.runner.OtestSbtFramework"))

//select utest
testFrameworks += new TestFramework("utest.runner.Framework")

//add upickle for json parsing
//libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.6"

//add "window" for the testing
jsDependencies += RuntimeDOM

scalaJSStage in Test := FastOptStage
