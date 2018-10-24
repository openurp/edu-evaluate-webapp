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
package org.openurp.edu.evaluation.app.lesson.service

import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.app.lesson.model.StdEvaluateSwitch
import org.openurp.edu.evaluation.app.department.model.EvaluateSwitch
import java.time.Instant

class StdEvaluateSwitchService(entityDao: EntityDao) {

  def getEvaluateSwitch(semester: Semester, project: Project): Seq[StdEvaluateSwitch] = {
    val query = OqlBuilder.from(classOf[StdEvaluateSwitch], "evaluateSwitch");
    query.where("evaluateSwitch.semester =:semester", semester);
    query.where("evaluateSwitch.project =:project", project);
    query.cacheable(true);
    entityDao.search(query);
  }

  def getOpenedSemesters(project: Project): Seq[Semester] = {
    val query = OqlBuilder.from[Semester](classOf[StdEvaluateSwitch].getName, "es")
    query.where("es.opened=true and es.project=:project", project)
      .where(":now between es.beginAt and es.endAt", Instant.now)
      .select("es.semester")

    entityDao.search(query);
  }

  //  def  isPassEvaluation(std:Student):Boolean = {
  //    val semesteres = getOpenSemesters();
  //    if (semesteres.isEmpty) true;
  //
  //    val hqlResult = "select evaluateResult.lesson.id"+ " from org.openurp.edu.teach.evaluate.course.model.EvaluateResult evaluateResult" + " where evaluateResult.student =:std and evaluateResult.semester in (:semesteres)";
  //    val hqlCourse = "select courseTake.lesson.id"+ " from org.openurp.edu.teach.lesson.model.CourseTaker courseTake"+ " where courseTake.std =:std and courseTake.lesson.semester in (:semesteres)"+ " and exists(from org.openurp.edu.teach.evaluate.course.model.QuestionnaireClazz questionnaireClazz"+ " where questionnaireClazz.lesson = courseTake.lesson)";
  //
  //    val queryResult = OqlBuilder.from(hqlResult);
  //    val queryCourse = OqlBuilder.from(hqlCourse);
  //    Map<String, Object> params = CollectUtils.newHashMap();
  //    params.put("std", std);
  //    params.put("semesteres", semesteres);
  //    queryResult.params(params);
  //    queryCourse.params(params);
  //    val  myClazzIds = entityDao.search(queryCourse);
  //    myClazzIds.removeAll(entityDao.search(queryResult));
  //    myClazzIds.isEmpty;
  //  }
}
