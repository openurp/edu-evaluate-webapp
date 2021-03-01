/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.qos.evaluation.clazz.web.action

import java.time.LocalDate

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.edu.code.model.StdType
import org.openurp.base.edu.model.Semester
import org.openurp.qos.evaluation.clazz.stat.model.ClazzEvalStat
import org.openurp.qos.evaluation.model.{EvaluationCriteria, QuestionType, Questionnaire}

class QuestionnaireStatSearchAction extends ProjectRestfulAction[ClazzEvalStat] {

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
    val query = OqlBuilder.from(classOf[Questionnaire], "questionnaire")
    put("questionnaires", entityDao.search(query))
    put("currentSemester",getCurrentSemester)
    put("evaluationCriterias", entityDao.getAll(classOf[EvaluationCriteria]))
    put("questionTypes", entityDao.getAll(classOf[QuestionType]))
    forward()
  }

  protected def getOptionMap(): collection.Map[String, Float] = {
    val optionNameMap = Collections.newMap[String, Float]
    optionNameMap.put("A", 90.toFloat)
    optionNameMap.put("B", 80.toFloat)
    optionNameMap.put("C", 60.toFloat)
    optionNameMap.put("D", 0.toFloat)
    optionNameMap
  }

}
