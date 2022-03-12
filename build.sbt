import org.openurp.parent.Settings._
import org.openurp.parent.Dependencies._
import org.beangle.tools.sbt.Sas

ThisBuild / organization := "org.openurp.qos.evaluation"
ThisBuild / version := "0.0.20-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/qos-evaluation"),
    "scm:git@github.com:openurp/qos-evaluation.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "chaostone",
    name  = "Tihua Duan",
    email = "duantihua@gmail.com",
    url   = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Std CreditBank"
ThisBuild / homepage := Some(url("http://openurp.github.io/qos-evaluation/index.html"))

val apiVer = "0.24.0"
val starterVer = "0.0.15"
val baseVer = "0.1.24"
val openurp_edu_api = "org.openurp.edu" % "openurp-edu-api" % apiVer
val openurp_qos_api = "org.openurp.qos" % "openurp-qos-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer

lazy val root = (project in file("."))
  .aggregate(core,questionnaire,clazz,department,webapp)

lazy val core = (project in file("core"))
  .settings(
    name := "openurp-qos-evaluation-core",
    common,
    libraryDependencies ++= Seq(openurp_edu_api,openurp_qos_api,beangle_ems_app,openurp_stater_web)
  )

lazy val questionnaire = (project in file("questionnaire"))
  .settings(
    name := "openurp-qos-evaluation-questionnaire",
    common,
    libraryDependencies ++= Seq(openurp_stater_web,openurp_base_tag),
  ).dependsOn(core)

lazy val clazz = (project in file("clazz"))
  .settings(
    name := "openurp-qos-evaluation-clazz",
    common,
    libraryDependencies ++= Seq(openurp_stater_web,openurp_base_tag),
  ).dependsOn(questionnaire)

lazy val department = (project in file("department"))
  .settings(
    name := "openurp-qos-evaluation-department",
    common,
    libraryDependencies ++= Seq(openurp_stater_web,openurp_base_tag),
  ).dependsOn(core)

lazy val webapp = (project in file("webapp"))
  .enablePlugins(WarPlugin)
  .settings(
    name := "openurp-qos-evaluation-webapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web,openurp_base_tag),
    libraryDependencies ++= Seq(Sas.Tomcat % "test")
  ).dependsOn(core,clazz,questionnaire,department)



