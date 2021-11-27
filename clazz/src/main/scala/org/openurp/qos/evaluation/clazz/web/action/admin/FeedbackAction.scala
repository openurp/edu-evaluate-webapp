/*
 * Copyright (C) 2005, The OpenURP Software.
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

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.edu.model.Semester
import org.openurp.qos.evaluation.clazz.model.Feedback
import org.openurp.starter.edu.helper.ProjectSupport

class FeedbackAction extends RestfulAction[Feedback] with ProjectSupport {

  override protected def indexSetting(): Unit = {
    put("project",getProject)
    put("departments", getDeparts)
    val semester = getId("semester") match {
      case Some(sid) => entityDao.get(classOf[Semester], sid.toInt)
      case None =>
        val query = OqlBuilder.from[Semester](classOf[Feedback].getName, "fb")
        query.select("fb.semester")
        query.limit(1, 2)
        query.orderBy("fb.semester.beginOn desc")
        val semesters = entityDao.search(query)
        if (semesters.isEmpty) {
          getCurrentSemester
        } else {
          semesters.head
        }
    }
    put("currentSemester", semester)
  }

  override protected def getQueryBuilder: OqlBuilder[Feedback] = {
    val builder = super.getQueryBuilder
    builder.where("feedback.teachDepart in(:departs)", getDeparts)
  }
}
