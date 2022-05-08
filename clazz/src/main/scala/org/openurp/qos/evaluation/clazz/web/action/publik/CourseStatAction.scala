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

package org.openurp.qos.evaluation.clazz.web.action.publik

import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.support.{ActionSupport, ParamSupport}
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.base.edu.code.model.CourseCategory
import org.openurp.base.model.Semester
import org.openurp.qos.evaluation.clazz.model.CourseEvalStat
import org.openurp.qos.evaluation.model.AssessGrade
import org.openurp.starter.edu.helper.ProjectSupport

class CourseStatAction extends ActionSupport with EntityAction[CourseEvalStat]  with ParamSupport with ProjectSupport {

  def index(): View = {
    put("project", getProject)
    put("grades", entityDao.getAll(classOf[AssessGrade]))
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
    forward()
  }

  def search(): View = {
    val builder=super.getQueryBuilder
    builder.where("courseEvalStat.publishStatus=2")
    put("courseEvalStats", entityDao.search(builder))
    forward()
  }

}
