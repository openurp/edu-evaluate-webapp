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
import org.openurp.edu.base.model.Semester
import org.beangle.commons.collection.Collections
import org.beangle.commons.collection.Order
import org.openurp.edu.evaluation.model.Option
import java.time.LocalDate
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.course.result.model.EvaluateResult
import org.openurp.edu.evaluation.course.result.model.QuestionResult
import org.openurp.edu.evaluation.course.stat.model.DepartEvalStat
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.course.model.Clazz
import org.beangle.webmvc.api.annotation.mapping

class DepartEvalSearchAction extends RestfulAction[DepartEvalStat] {
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
    val departEvalStat = OqlBuilder.from(classOf[DepartEvalStat], "departEvalStat")
    populateConditions(departEvalStat)
    departEvalStat.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    departEvalStat.where("departEvalStat.lesson.semester=:semester", semester)
    put("departEvalStats", entityDao.search(departEvalStat))
    forward()
  }

  @mapping(value = "{id}")
  override def info(id: String): View = {
    val questionnaireStat = entityDao.get(classOf[DepartEvalStat], id.toLong)
    put("questionnaireStat", questionnaireStat);
    // zongrenci fix
    val query = OqlBuilder.from[Array[Any]](classOf[EvaluateResult].getName, "result");
    query.where("result.department =:tea", questionnaireStat.department);
    //    query.where("result.lesson.course=:course", questionnaireStat.course)
    query.select("case when result.statType =1 then count(result.id) end,count(result.id)");
    query.groupBy("result.statType");
    entityDao.search(query) foreach { a =>
      put("number1", a(0))
      put("number2", a(1))
    }
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
    val querys = OqlBuilder.from[Long](classOf[Clazz].getName, "lesson");
    querys.join("lesson.teachers", "teacher");
    //    querys.where("teacher=:teach",questionnaireStat.teacher);
    querys.where("lesson.teachDepart=:depart", questionnaireStat.department);
    querys.join("lesson.teachclass.courseTakers", "courseTaker");
    querys.select("count(courseTaker.id)");
    val numbers = entityDao.search(querys)(0)
    put("numbers", entityDao.search(querys)(0));
    val que = OqlBuilder.from(classOf[QuestionResult], "questionR");
    //    que.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    que.where("questionR.result.department=:depart", questionnaireStat.department);
    que.select("questionR.question.id,questionR.option.id,count(*)");
    que.groupBy("questionR.question.id,questionR.option.id");
    put("questionRs", entityDao.search(que));
    val quer = OqlBuilder.from(classOf[QuestionResult], "questionR");
    //    quer.where("questionR.result.teacher=:teaId", questionnaireStat.teacher);
    quer.where("questionR.result.department=:depart", questionnaireStat.department);
    quer.select("questionR.question.id,questionR.question.content,sum(questionR.score)/count(questionR.id)*100");
    quer.groupBy("questionR.question.id,questionR.question.content");
    put("questionResults", entityDao.search(quer));
    forward()
  }
}
