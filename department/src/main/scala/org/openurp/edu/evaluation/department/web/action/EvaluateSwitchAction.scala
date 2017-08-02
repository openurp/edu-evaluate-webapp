package org.openurp.edu.evaluation.department.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import java.time.LocalDate

/**
 * @author xinzhou
 */
class EvaluateSwitchAction extends ProjectRestfulAction[EvaluateSwitch] {

  override def indexSetting(): Unit = {
    put("semesters", getSemesters())
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def editSetting(entity: EvaluateSwitch): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

}
