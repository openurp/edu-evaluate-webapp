package org.openurp.edu.evaluation.department.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.department.model.EvaluateSwitch
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire

/**
 * @author xinzhou
 */
class EvaluateSwitchAction extends RestfulAction[EvaluateSwitch] {

  override def indexSetting(): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

  override def editSetting(entity: EvaluateSwitch): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

}