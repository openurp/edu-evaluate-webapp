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

package org.openurp.qos.evaluation.app.department.helper

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.doc.transfer.importer.{ImportListener, ImportResult}
import org.beangle.security.Securities
import org.openurp.base.hr.model.Teacher
import org.openurp.base.model.Semester
import org.openurp.qos.evaluation.base.model.Questionnaire
import org.openurp.qos.evaluation.department.model.DepartEvaluate

import java.time.Instant

/**
 * @author xinzhou
 */
class ImportDepartListener(entityDao: EntityDao) extends ImportListener {
  override def onItemStart(tr: ImportResult): Unit = {
    val teacherCode = transfer.curData("teacher.staff.code")
    val semesterCode = transfer.curData("semester.code").toString()
    val departmentId = getTeacher().department.id
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[DepartEvaluate], "de")
      builder.where("de.teacher.staff.code=:code and de.semester.code=:scode and de.department.id=:id", teacherCode, semesterCode, departmentId)
      entityDao.search(builder) foreach { s =>
        this.transfer.current = s
      }
    }
  }

  def getTeacher(): Teacher = {
    val teachers = entityDao.findBy(classOf[Teacher], "code", List(Securities.user))
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teacher with code " + Securities.user)
    } else {
      teachers.head
    }
  }

  override def onItemFinish(tr: ImportResult): Unit = {
    val departEvaluate = tr.transfer.current.asInstanceOf[DepartEvaluate]
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    departEvaluate.questionnaire = questionnaire
    departEvaluate.evaluateAt = Instant.now
    departEvaluate.department = getTeacher().department
    entityDao.saveOrUpdate(departEvaluate)
  }
}
