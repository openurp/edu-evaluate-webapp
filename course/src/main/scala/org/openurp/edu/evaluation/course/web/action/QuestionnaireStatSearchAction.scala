package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.code.model.Education
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.lesson.stat.model.LessonEvalStat
import org.beangle.commons.collection.Collections
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.model.Questionnaire
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class QuestionnaireStatSearchAction extends RestfulAction[LessonEvalStat] {

  override def index(): View = {
    val stdType = entityDao.get(classOf[StdType], 5)
    put("stdTypeList", stdType)
    val department = entityDao.get(classOf[Department], 20)
    put("departmentList", department)

    var searchFormFlag = get("searchFormFlag").orNull
    if (searchFormFlag == null) {
      searchFormFlag = "beenStat"
    }
    put("searchFormFlag", searchFormFlag)
    put("departments", entityDao.getAll(classOf[Department]))
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire").where("questionnaire.state =:state", true)
    put("questionnaires", entityDao.search(query))
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    put("evaluationCriterias", entityDao.getAll(classOf[EvaluationCriteria]))
    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
    forward()
  }

  protected def getOptionMap(): collection.Map[String, Float] = {
    val optionNameMap = Collections.newMap[String, Float]
    optionNameMap.put("A", 90.toFloat);
    optionNameMap.put("B", 80.toFloat);
    optionNameMap.put("C", 60.toFloat);
    optionNameMap.put("D", 0.toFloat);
    optionNameMap
  }

}