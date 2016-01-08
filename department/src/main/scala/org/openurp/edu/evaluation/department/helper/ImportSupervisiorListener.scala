package org.openurp.edu.evaluation.department.helper

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.importer.listener.ItemImporterListener
import org.openurp.edu.base.model.Student
import org.beangle.data.transfer.TransferResult
import org.openurp.hr.base.model.Staff
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.base.model.Semester
import java.util.Date
import org.openurp.edu.evaluation.model.Questionnaire

/**
 * @author xinzhou
 */
class ImportSupervisiorListener(entityDao: EntityDao) extends ItemImporterListener {
  override def onItemStart(tr: TransferResult) {
    val staffCode = importer.curData.get("staff.code").get
    val semesterCode = importer.curData.get("semester.code").get.toString()
    val departmentCode = importer.curData.get("department.code").get
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[SupervisiorEvaluate], "s").where("s.staff.code=:code and s.semester.code=:scode and s.department.code=:dcode", staffCode, semesterCode, departmentCode)
      entityDao.search(builder) foreach { s =>
        this.importer.current = s
      }
    }
  }

  override def onItemFinish(tr: TransferResult) {
    val supervisiorEvaluate = tr.transfer.current.asInstanceOf[SupervisiorEvaluate]
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    supervisiorEvaluate.questionnaire = questionnaire
    supervisiorEvaluate.evaluateAt = new Date()
    entityDao.saveOrUpdate(supervisiorEvaluate)
  }
}