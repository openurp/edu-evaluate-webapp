package org.openurp.edu.evaluation.department.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.beangle.commons.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire

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
