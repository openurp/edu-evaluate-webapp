/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.qos.evaluation.clazz.web.action.admin

import org.beangle.commons.io.DataType
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportSetting
import org.beangle.ems.app.Ems
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.edu.code.model.CourseCategory
import org.openurp.base.model.Semester
import org.openurp.qos.evaluation.clazz.model.{CategoryEvalStat, CourseEvalStat, DepartEvalStat}
import org.openurp.qos.evaluation.clazz.web.helper.StatCoursePropertyExtractor
import org.openurp.qos.evaluation.model.AssessGrade
import org.openurp.starter.edu.helper.ProjectSupport

class CourseStatAction extends RestfulAction[CourseEvalStat] with ProjectSupport {

  override protected def indexSetting(): Unit = {
    put("project", getProject)
    put("grades", entityDao.getAll(classOf[AssessGrade]))
    put("departments", getDeparts)
    put("categories", getCodes(classOf[CourseCategory]))
    val semester = getId("semester") match {
      case Some(sid) => entityDao.get(classOf[Semester], sid.toInt)
      case None =>
        val query = OqlBuilder.from[Semester](classOf[CourseEvalStat].getName, "stat")
        query.select("stat.semester")
        query.limit(1, 2)
        query.orderBy("stat.semester.beginOn desc")
        val semesters = entityDao.search(query)
        if (semesters.isEmpty) {
          getCurrentSemester
        } else {
          semesters.head
        }
    }
    put("currentSemester", semester)
  }

  override protected def getQueryBuilder: OqlBuilder[CourseEvalStat] = {
    put("webapp_base", Ems.webapp)
    val builder = super.getQueryBuilder
    addDepart(builder, "courseEvalStat.teachDepart.id")
    builder
  }

  def history(): View = {
    val q = OqlBuilder.from(classOf[CourseEvalStat], "c")
    q.where("c.teacher.id=:teacherId", longId("teacher"))
    q.orderBy("c.semester.beginOn desc")
    put("stats", entityDao.search(q))
    forward()
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val stat = entityDao.get(classOf[CourseEvalStat], id.toLong)
    put("stat", stat)
    val query = OqlBuilder.from(classOf[CategoryEvalStat], "stat")
    query.where("stat.category=:category", stat.category)
    query.where("stat.semester=:semester", stat.semester)
    val categoryStats = entityDao.search(query)
    if (categoryStats.nonEmpty) {
      put("categoryStat", categoryStats.head)
      val departQuery = OqlBuilder.from(classOf[DepartEvalStat], "ces")
      departQuery.where("ces.project=:project", stat.project)
      departQuery.where("ces.semester=:semester", stat.semester)
      departQuery.where("ces.department=:department", stat.teachDepart)
      put("departEvalStat", entityDao.search(departQuery).head)
      forward("report")
    } else {
      forward()
    }
  }

  override def configExport(setting: ExportSetting): Unit = {
    super.configExport(setting)
    //    val writer = new ExcelItemWriter(setting.context, response.getOutputStream)
    //    setting.writer = writer
    //    writer.registerFormat(DataType.Float, "#,##0.00")
    setting.context.extractor = new StatCoursePropertyExtractor
  }
}
