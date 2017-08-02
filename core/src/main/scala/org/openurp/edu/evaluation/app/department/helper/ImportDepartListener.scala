package org.openurp.edu.evaluation.department.helper

import java.time.Instant
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.TransferResult
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.platform.api.security.Securities
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.beangle.data.transfer.AbstractTransferListener

/**
 * @author xinzhou
 */
class ImportDepartListener(entityDao: EntityDao) extends AbstractTransferListener {
  override def onItemStart(tr: TransferResult) {
    val teacherCode = transfer.curData.get("teacher.code").get
    val semesterCode = transfer.curData.get("semester.code").get.toString()
    val departmentId = getTeacher().user.department.id
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[DepartEvaluate], "de")
      builder.where("de.teacher.code=:code and de.semester.code=:scode and de.department.id=:id", teacherCode, semesterCode, departmentId)
      entityDao.search(builder) foreach { s =>
        this.transfer.current = s
      }
    }
  }  

  override def onItemFinish(tr: TransferResult) {
    val departEvaluate = tr.transfer.current.asInstanceOf[DepartEvaluate]
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    departEvaluate.questionnaire = questionnaire
    departEvaluate.evaluateAt = Instant.now
    departEvaluate.department = getTeacher().user.department
    entityDao.saveOrUpdate(departEvaluate)
  }

  def getTeacher(): Teacher = {
    val teachers = entityDao.findBy(classOf[Teacher], "code", List(Securities.user))
    if (teachers.isEmpty) {
      throw new RuntimeException("Cannot find teacher with code " + Securities.user)
    } else {
      teachers.head
    }
  }
}
