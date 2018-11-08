/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.course.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.evaluation.model.EvaluationCriteria
import org.beangle.data.dao.OqlBuilder
import org.openurp.code.edu.model.EducationLevel
import org.openurp.edu.evaluation.model.QuestionType
import org.openurp.edu.evaluation.clazz.stat.model.ClazzEvalStat
import org.beangle.commons.collection.Collections
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.evaluation.model.Questionnaire
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class QuestionnaireStatSearchAction extends RestfulAction[ClazzEvalStat] {

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
