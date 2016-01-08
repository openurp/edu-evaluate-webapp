package org.openurp.edu.evaluation.department.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.department.model.EvaluateSwitch
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.beangle.commons.text.inflector.en.EnNounPluralizer

/**
 * @author xinzhou
 */
class EvaluateSwitchAction extends RestfulAction[EvaluateSwitch] {

  override def indexSetting(): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def editSetting(entity: EvaluateSwitch): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

}
