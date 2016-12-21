package org.openurp.edu.evaluation.app.lesson.service

import org.beangle.data.dao.EntityDao
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.app.lesson.model.StdEvaluateSwitch

class StdEvaluateSwitchService(entityDao: EntityDao) {

  def getEvaluateSwitch(semester: Semester, project: Project): Seq[StdEvaluateSwitch] = {
    val query = OqlBuilder.from(classOf[StdEvaluateSwitch], "evaluateSwitch");
    query.where("evaluateSwitch.semester =:semester", semester);
    query.where("evaluateSwitch.project =:project", project);
    query.cacheable(true);
    entityDao.search(query);
  }

  def getOpenSemesters(): Seq[Semester] = {
    val hql = "select distinct evaluateSwitch.semester" + " from org.openurp.edu.teach.evaluate.course.model.EvaluateSwitch evaluateSwitch" + " where evaluateSwitch = true and :date between between.beginOn and evaluateSwitch.endOn";
    val query = OqlBuilder.from(hql);
    query.param("date", new java.util.Date());
    entityDao.search(query);
  }

  //  def  isPassEvaluation(std:Student):Boolean = {
  //    val semesteres = getOpenSemesters();
  //    if (semesteres.isEmpty) true;
  //
  //    val hqlResult = "select evaluateResult.lesson.id"+ " from org.openurp.edu.teach.evaluate.course.model.EvaluateResult evaluateResult" + " where evaluateResult.student =:std and evaluateResult.semester in (:semesteres)";
  //    val hqlCourse = "select courseTake.lesson.id"+ " from org.openurp.edu.teach.lesson.model.CourseTaker courseTake"+ " where courseTake.std =:std and courseTake.lesson.semester in (:semesteres)"+ " and exists(from org.openurp.edu.teach.evaluate.course.model.QuestionnaireLesson questionnaireLesson"+ " where questionnaireLesson.lesson = courseTake.lesson)";
  //
  //    val queryResult = OqlBuilder.from(hqlResult);
  //    val queryCourse = OqlBuilder.from(hqlCourse);
  //    Map<String, Object> params = CollectUtils.newHashMap();
  //    params.put("std", std);
  //    params.put("semesteres", semesteres);
  //    queryResult.params(params);
  //    queryCourse.params(params);
  //    val  myLessonIds = entityDao.search(queryCourse);
  //    myLessonIds.removeAll(entityDao.search(queryResult));
  //    myLessonIds.isEmpty;
  //  }
}
