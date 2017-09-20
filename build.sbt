import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import ScalateKeys._
import com.typesafe.sbt.packager.archetypes.JavaAppPacking

val ScalatraVersion = "2.5.1"

ScalatraPlugin.scalatraSettings

scalateSettings

organization := "io.kokoro.bot.kfc"

name := "kfc"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.3"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "compile;container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime"
)

scalateTemplateConfig in Compile := {
  val base = (sourceDirectory in Compile).value
  Seq(
    TemplateConfig(
      base / "webapp" / "WEB-INF" / "templates",
      Seq.empty,  /* default imports should be added here */
      Seq(
        Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
      ),  /* add extra bindings here */
      Some("templates")
    )
  )
}

enablePlugins(JettyPlugin)
enablePlugins(JavaAppPacking)
