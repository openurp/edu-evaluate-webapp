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

package org.openurp.qos.evaluation.teacher.web.action

import java.text.DecimalFormat
import java.time.LocalDate

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.edu.model.{Semester, Teacher}
import org.openurp.edu.clazz.model.{Clazz, CourseTaker}
import org.openurp.qos.evaluation.app.course.model.EvaluateSearchDepartment
import org.openurp.qos.evaluation.clazz.model.EvaluateResult
import org.openurp.starter.edu.helper.ProjectSupport

class EvaluateStatusTeacherAction extends RestfulAction[EvaluateResult] with ProjectSupport {

  override def index(): View = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  /**
   * 教师评教回收情况
   * //
   */
  override def search(): View = {
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val teacher = getUser(classOf[Teacher])
    // 得到院系下的所有级教学任务
    val clazzQuery = OqlBuilder.from(classOf[Clazz], "clazz")
    clazzQuery.where("clazz.semester.id=:semesterId", semesterId)
    if (teacher != null) {
      clazzQuery.join("clazz.teachers", "teacher")
      clazzQuery.where("teacher =:teacher", teacher)
    }
    val clazzList = entityDao.search(clazzQuery)
    val evaluateSearchDepartmentList = Collections.newBuffer[EvaluateSearchDepartment]
    clazzList foreach { clazz =>
      var countAll: Long = 0L
      var haveFinish: Long = 0L
      val query = OqlBuilder.from[Long](classOf[CourseTaker].getName, "courseTake")
      query.select("select count(*)")
      query.where("courseTake.clazz =:clazz", clazz)
      val list = entityDao.search(query)
      // 得到指定学期，院系的学生评教人次总数
      countAll = list(0)

      val query1 = OqlBuilder.from[Long](classOf[EvaluateResult].getName, "rs")
      query1.select("select count(*)")
      query1.where("rs.clazz =:clazz", clazz)
      val list1 = entityDao.search(query1)
      // 得到指定学期，已经评教的学生人次数
      haveFinish = list1(0)
      var finishRate = ""
      if (countAll != 0) {
        val df = new DecimalFormat("0.0")
        finishRate = df.format((haveFinish * 100 / countAll).toFloat) + "%"
      }
      val esd = new EvaluateSearchDepartment()
      esd.semester = semester
      esd.clazz = clazz
      esd.countAll = countAll
      esd.haveFinish = haveFinish
      esd.finishRate = finishRate
      evaluateSearchDepartmentList += esd

    }
    // Collections.sort(evaluateSearchDepartmentList, new PropertyComparator("adminClass.code"))
    evaluateSearchDepartmentList.sortWith((x, y) => x.clazz.course.code < y.clazz.course.code)
    put("evaluateSearchDepartmentList", evaluateSearchDepartmentList)
    put("semester", semester)
    forward()
  }

}
