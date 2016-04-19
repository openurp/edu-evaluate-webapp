package org.openurp.edu.evaluation.department.helper

import java.util.Date
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.TransferResult
import org.beangle.data.transfer.importer.listener.ItemImporterListener
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.platform.api.security.Securities
import org.openurp.edu.base.model.Teacher

/**
 * @author xinzhou
 */
class ImportDepartListener(entityDao: EntityDao) extends ItemImporterListener {
  override def onItemStart(tr: TransferResult) {
    val staffCode = importer.curData.get("staff.code").get
    val semesterCode = importer.curData.get("semester.code").get.toString()
    val departmentId = getTeacher().department.id
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[DepartEvaluate], "de")
      builder.where("de.staff.code=:code and de.semester.code=:scode and de.department.id=:id", staffCode, semesterCode, departmentId)
      entityDao.search(builder) foreach { s =>
        this.importer.current = s
      }
    }
  }

  override def onItemFinish(tr: TransferResult) {
    val departEvaluate = tr.transfer.current.asInstanceOf[DepartEvaluate]
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    departEvaluate.questionnaire = questionnaire
    departEvaluate.evaluateAt = new Date()
    departEvaluate.department = getTeacher().department
    entityDao.saveOrUpdate(departEvaluate)
  }

  def getTeacher(): Teacher = {
    val staffs = entityDao.findBy(classOf[Teacher], "code", List(Securities.user))
    if (staffs.isEmpty) {
      throw new RuntimeException("Cannot find teacher with code " + Securities.user)
    } else {
      staffs.head
    }
  }
}
