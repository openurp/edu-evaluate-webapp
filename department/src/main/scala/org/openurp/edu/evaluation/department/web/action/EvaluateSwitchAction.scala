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
package org.openurp.edu.evaluation.department.web.action

import java.time.LocalDate

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.web.ProjectSupport

/**
 * @author xinzhou
 */
class EvaluateSwitchAction extends  RestfulAction[EvaluateSwitch] with ProjectSupport {

  override def indexSetting(): Unit = {
    put("semesters",  entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
    put("currentSemester",getCurrentSemester)
  }

  override def editSetting(entity: EvaluateSwitch): Unit = {
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("questionnaires", entityDao.getAll(classOf[Questionnaire]))
  }

}
