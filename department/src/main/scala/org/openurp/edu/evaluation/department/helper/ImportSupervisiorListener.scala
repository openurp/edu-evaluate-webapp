package org.openurp.edu.evaluation.department.helper

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Student
import org.openurp.edu.evaluation.department.model.SupervisiorEvaluate
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import java.time.Instant
import org.beangle.data.transfer.importer.AbstractImportListener
import org.beangle.data.transfer.importer.ImportResult

/**
 * @author xinzhou
 */
class ImportSupervisiorListener(entityDao: EntityDao) extends AbstractImportListener {
  override def onItemStart(tr: ImportResult) {
    val teacherCode = transfer.curData.get("teacher.code").get
    val semesterCode = transfer.curData.get("semester.code").get.toString()
    val departmentCode = transfer.curData.get("department.code").get
    val semesterBuilder = OqlBuilder.from(classOf[Semester], "s").where("s.code=:code ", semesterCode)
    val semesters = entityDao.search(semesterBuilder)
    if (semesters.isEmpty) {
      tr.addFailure("学期数据格式非法", semesterCode)
    } else {
      val builder = OqlBuilder.from(classOf[SupervisiorEvaluate], "s").where("s.teacher.code=:code and s.semester.code=:scode and s.department.code=:dcode", teacherCode, semesterCode, departmentCode)
      entityDao.search(builder) foreach { s =>
        this.transfer.current = s
      }
    }
  }

  override def onItemFinish(tr: ImportResult) {
    val supervisiorEvaluate = tr.transfer.current.asInstanceOf[SupervisiorEvaluate]
    val questionnaire = entityDao.get(classOf[Questionnaire], 322L)
    supervisiorEvaluate.questionnaire = questionnaire
    supervisiorEvaluate.evaluateAt = Instant.now
    entityDao.saveOrUpdate(supervisiorEvaluate)
  }
}
