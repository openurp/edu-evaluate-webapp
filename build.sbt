import org.openurp.parent.Dependencies.*
import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.qos.evaluation"
ThisBuild / version := "0.0.23-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/qos-evaluation"),
    "scm:git@github.com:openurp/qos-evaluation.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP QoS Evalution"
ThisBuild / homepage := Some(url("http://openurp.github.io/qos-evaluation/index.html"))

val apiVer = "0.41.6"
val starterVer = "0.3.40"
val baseVer = "0.4.38"
val openurp_edu_api = "org.openurp.edu" % "openurp-edu-api" % apiVer
val openurp_qos_api = "org.openurp.qos" % "openurp-qos-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer
val beangle_webmvc ="org.beangle.webmvc" % "beangle-webmvc" % "0.9.33-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin, UndertowPlugin)
  .settings(
    name := "openurp-qos-evaluation-webapp",
    common,
    libraryDependencies ++= Seq(openurp_edu_api, openurp_qos_api, beangle_ems_app, openurp_stater_web),
    libraryDependencies ++= Seq(openurp_stater_web, openurp_base_tag,beangle_webmvc)
  )
