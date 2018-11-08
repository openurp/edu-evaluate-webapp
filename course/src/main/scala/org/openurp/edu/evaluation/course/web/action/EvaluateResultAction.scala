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

import org.beangle.commons.lang.Numbers
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.base.model.Project
import org.springframework.beans.support.PropertyComparator
import org.beangle.commons.collection.Order
import org.openurp.edu.base.model.Teacher
import java.time.LocalDate
import org.openurp.edu.evaluation.clazz.result.model.EvaluateResult
import org.openurp.edu.evaluation.clazz.result.model.QuestionResult
import org.openurp.edu.course.model.ExamTaker

class EvaluateResultAction extends ProjectRestfulAction[EvaluateResult] {

  override protected def indexSetting(): Unit = {
    put("semesters", getSemesters())
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val statType = getInt("statType").getOrElse(null)
    val evaluateResult = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    populateConditions(evaluateResult)
    evaluateResult.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    evaluateResult.where("evaluateResult.clazz.semester=:semester", semester)
    if (statType != null) {
      evaluateResult.where("evaluateResult.statType=:statType", statType)
    }
    put("evaluateResults", entityDao.search(evaluateResult))
    forward()
  }

  //  @Override
  // override protected def getQueryBuilder():OqlBuilder[EvaluateResult] ={
  //    val department = entityDao.get(classOf[Department],20)
  //    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult");
  //    populateConditions(query);
  //    query.where("evaluateResult.clazz.teachDepart in (:teachDeparts)", department)
  ////    query.limit(getPageLimit());
  //
  //  }

  /**
   * 修改(评教结果,是否有效)
   *
   * @return
   */
  def updateState(): View = {
    val ids = longIds("evaluateResult")
    var state = getInt("isEvaluate").get
    //    if (state) {
    //      state = false
    //    }
    val results = entityDao.find(classOf[EvaluateResult], ids);
    results foreach { result =>
      result.statType = state
      //      //FIXME
      //    result.statType = 1
    }
    try {
      entityDao.saveOrUpdate(results);
      return redirect("search", "info.action.success");
    } catch {
      case e: Exception =>
        return redirect("search", "info.save.failure");
    }
  }

  /**
   * 更改教师
   */
  def updateTeacher(): View = {
    put("evaluateR", entityDao.get(classOf[EvaluateResult], getLong("evaluateResult").get))
    // put("departments",entityDao.get(Department.class, "",));
    forward()
  }

  def saveEvaluateTea(): View = {
    try {
      val evaluateR = entityDao.get(classOf[EvaluateResult], getLong("evaluateResult").get)
      evaluateR.teacher = entityDao.get(classOf[Teacher], getLong("teacher.id").get)
      entityDao.saveOrUpdate(evaluateR);
    } catch {
      case e: Exception =>
        // TODO: handle exception
        return redirect("search", "info.save.failure");
    }
    return redirect("search", "info.action.success");
  }

  /**
   * 查看(详细信息)
   */
  override def info(@param("id") id: String): View = {
    //    val entityId = longId("evaluateResult")
    if (null == id) {
      logger.warn("cannot get paremeter {}Id or {}.id")
    }
    val resultId = Numbers.toLong(id)
    val result = entityDao.get(classOf[EvaluateResult], Numbers.toLong(id))
    val query = OqlBuilder.from(classOf[QuestionResult], "questionResult")
    query.where("questionResult.result =:result", result)
    val questionResults = entityDao.search(query)
    //          questions.sortWith((x, y) => x.priority < y.priority)
    questionResults.sortWith((x, y) => x.question.priority < y.question.priority)
    //    questionResults.sort(questionResults, new PropertyComparator("question"))
    put("questionResults", questionResults)
    put("remark", result.remark)
    forward()
  }

  /**
   * 将没有考试资格的学生评教问卷改为无效
   *
   * @return
   */
  def changeToInvalid(): View = {

    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    query.where("evaluateResult.clazz.semester.id = :semesterId", semesterId)
    val results = entityDao.search(query)
    try {
      results foreach { result =>
        //      for (EvaluateResult result : results) {
        //        // TODO kang 怎么确定学生有没有一门课的考试资格
        val builder = OqlBuilder.from(classOf[ExamTaker], "examTaker")
        builder.where("examTaker.clazz=:clazz", result.clazz)
        builder.where("examTaker.std=:std", result.student)
        val takes = entityDao.search(builder);
        if (takes.size > 0) {
          if ("违纪".equals(takes.head.examStatus.name)) {
            //FIXME
            //result.statType=false
            result.statType = 0
          }
        }
      }
      saveOrUpdate(results);
    } catch {
      case e: Exception =>
        return redirect("search", "info.action.failure");
    }
    return redirect("search", "info.action.success");
  }
}
