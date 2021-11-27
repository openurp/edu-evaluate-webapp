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

package org.openurp.qos.evaluation.clazz.web.action.teacher

import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.EntityAction
import org.openurp.base.edu.model.{Project, Semester, Teacher}
import org.openurp.qos.evaluation.clazz.model.Feedback
import org.openurp.qos.evaluation.clazz.web.helper.ClazzFeedback
import org.openurp.starter.edu.helper.ProjectSupport

import java.time.LocalDate

class FeedbackAction extends EntityAction[Feedback] with ProjectSupport {

  def index(): View = {
    val me = getTeacher()
    put("project", me.projects.head)
    val semester = getId("semester") match {
      case Some(sid) => entityDao.get(classOf[Semester], sid.toInt)
      case None => getCurrentSemester(me.projects.head)
    }
    put("currentSemester", semester)
    val fbQuery = OqlBuilder.from(classOf[Feedback], "fb")
    fbQuery.where("fb.semester=:semester", semester)
    fbQuery.where("fb.teacher=:teacher", me)
    val fds = entityDao.search(fbQuery)
    val feedbacks = fds.groupBy(_.crn).map { case (k, l) =>
      new ClazzFeedback(k, l.head.course, l.sortBy(_.updatedAt).reverse)
    }
    put("stdCount", fds.map(_.std).distinct.size)
    put("feedbacks", feedbacks)
    forward()
  }

  private def getTeacher(): Teacher = {
    val query = OqlBuilder.from(classOf[Teacher], "t")
    query.where("t.user.code=:code", Securities.user)
    entityDao.search(query).head
  }

  def getCurrentSemester(project: Project): Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
      .where("semester.calendar in(:calendars)", project.calendars)
    builder.where(":date between semester.beginOn and  semester.endOn", LocalDate.now)
    builder.cacheable()
    val rs = entityDao.search(builder)
    if (rs.isEmpty) { //如果没有正在其中的学期，则查找一个距离最近的
      val builder2 = OqlBuilder.from(classOf[Semester], "semester")
        .where("semester.calendar in(:calendars)", project.calendars)
      builder2.orderBy("abs(semester.beginOn - current_date() + semester.endOn - current_date())")
      builder2.cacheable()
      builder2.limit(1, 1)
      entityDao.search(builder2).headOption.orNull
    } else {
      rs.head
    }
  }
}
