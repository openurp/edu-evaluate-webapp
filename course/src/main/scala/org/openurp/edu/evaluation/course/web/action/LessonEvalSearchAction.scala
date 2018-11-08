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
package org.openurp.edu.evaluation.course.web.action

import java.time.LocalDate

import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.clazz.stat.model.ClazzEvalStat
import org.openurp.edu.evaluation.model.Option

class ClazzEvalSearchAction extends RestfulAction[ClazzEvalStat] {

  override def index(): View = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    forward()
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val clazzEvalStat = OqlBuilder.from(classOf[ClazzEvalStat], "clazzEvalStat")
    populateConditions(clazzEvalStat)
    clazzEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    clazzEvalStat.where("clazzEvalStat.clazz.semester=:semester", semester)
    put("clazzEvalStats", entityDao.search(clazzEvalStat))
    forward()
  }

  @mapping(value = "{id}")
  override def info(@param("id") id: String): View = {
    val questionnaireStat = entityDao.get(classOf[ClazzEvalStat], java.lang.Long.parseLong(id));
    put("questionnaireStat", questionnaireStat);

    val list = Collections.newBuffer[Option]
    val questions = questionnaireStat.questionnaire.questions
    questions foreach { question =>
      val options = question.optionGroup.options
      options foreach { option =>
        var tt = 0
        list foreach { oldOption =>
          if (oldOption.id == option.id) {
            tt += 1;
          }
        }
        if (tt == 0) {
          list += option
        }
      }
    }
    put("options", list);
    put("questionnaireStat", questionnaireStat);
    forward()
  }

}
