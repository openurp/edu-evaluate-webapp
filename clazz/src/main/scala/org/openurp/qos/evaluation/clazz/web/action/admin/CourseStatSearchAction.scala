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
import org.beangle.web.action.annotation.{mapping, param}
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.base.edu.code.CourseCategory
import org.openurp.base.model.{Project, Semester}
import org.openurp.qos.evaluation.clazz.model.{CategoryEvalStat, CourseEvalStat, DepartEvalStat}
import org.openurp.qos.evaluation.clazz.web.helper.StatCoursePropertyExtractor
import org.openurp.qos.evaluation.config.AssessGrade
import org.openurp.starter.web.support.ProjectSupport

class CourseStatSearchAction extends RestfulAction[CourseEvalStat], ExportSupport[CourseEvalStat], ProjectSupport {
  override protected def indexSetting(): Unit = {
    given project: Project = getProject

    put("project", project)
    put("grades", entityDao.getAll(classOf[AssessGrade]))
    put("categories", getCodes(classOf[CourseCategory]))
    put("currentSemester", getSemester)
  }

  override protected def getQueryBuilder: OqlBuilder[CourseEvalStat] = {
    val builder = super.getQueryBuilder
    addDepart(builder, "courseEvalStat.teachDepart.id")
    builder.where("courseEvalStat.publishStatus>=1")
    builder
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val stat = entityDao.get(classOf[CourseEvalStat], id.toLong)
    val query = OqlBuilder.from(classOf[CategoryEvalStat], "stat")
    query.where("stat.category=:category", stat.category)
    query.where("stat.semester=:semester", stat.semester)
    val categoryStats = entityDao.search(query)
    put("categoryStat", categoryStats.head)

    val departQuery = OqlBuilder.from(classOf[DepartEvalStat], "ces")
    departQuery.where("ces.project=:project", stat.project)
    departQuery.where("ces.semester=:semester", stat.semester)
    departQuery.where("ces.department=:department", stat.teachDepart)
    put("departEvalStat", entityDao.search(departQuery).head)

    put("stat", stat)
    forward()
  }

  override def configExport(setting: ExportSetting): Unit = {
    super.configExport(setting)
    //    val writer = new ExcelItemWriter(setting.context, response.getOutputStream)
    //    setting.writer = writer
    //    writer.registerFormat(DataType.Float, "#,##0.00")
    setting.context.extractor = new StatCoursePropertyExtractor
  }
}
