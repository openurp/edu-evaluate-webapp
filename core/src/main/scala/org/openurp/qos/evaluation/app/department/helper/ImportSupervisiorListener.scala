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

import java.time.Instant

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.data.transfer.importer.{ImportListener, ImportResult}
import org.openurp.base.model.Semester
import org.openurp.qos.evaluation.department.model.SupervisiorEvaluate
import org.openurp.qos.evaluation.base.model.Questionnaire

/**
 * @author xinzhou
 */
class ImportSupervisiorListener(entityDao: EntityDao) extends ImportListener {
  override def onItemStart(tr: ImportResult): Unit = {
    val teacherCode = transfer.curData("teacher.staff.code")
    val semesterCode = transfer.curData("semester.code").toString
    val departmentCode = transfer.curData("department.code")
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[SupervisiorEvaluate], "s").where("s.teacher.staff.code=:code and s.semester.code=:scode and s.department.code=:dcode", teacherCode, semesterCode, departmentCode)
      entityDao.search(builder) foreach { s =>
        this.transfer.current = s
      }
    }
  }

  override def onItemFinish(tr: ImportResult) : Unit = {
    val supervisiorEvaluate = tr.transfer.current.asInstanceOf[SupervisiorEvaluate]
    //FIXME magic number
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    supervisiorEvaluate.questionnaire = questionnaire
    supervisiorEvaluate.evaluateAt = Instant.now
    entityDao.saveOrUpdate(supervisiorEvaluate)
  }
}
