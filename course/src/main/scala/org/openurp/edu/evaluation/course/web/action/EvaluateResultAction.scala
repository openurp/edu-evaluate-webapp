package org.openurp.edu.evaluation.course.web.action

import org.beangle.commons.lang.Numbers
import org.beangle.commons.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.openurp.edu.base.code.model.StdType
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.EvaluateResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.evaluation.lesson.result.model.QuestionResult
import org.openurp.edu.lesson.model.ExamTaker
import org.springframework.beans.support.PropertyComparator
import org.beangle.commons.collection.Order
import org.openurp.edu.base.model.Teacher

class EvaluateResultAction extends RestfulAction[EvaluateResult] {

  //  override def  index() : String = {
  //    val semester = entityDao.get(classOf[Semester],20141)
  //    put("semester",semester)
  //    val project = entityDao.get(classOf[Project],1)
  //    put("project",project)
  //    val stdType = entityDao.get(classOf[StdType],5)
  //    put("stdTypeList", stdType)
  //    val department = entityDao.get(classOf[Department],20)
  //    put("departmentList", department);
  //    forward();
  //  }
  override protected def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    put("currentSemester", entityDao.search(semesterQuery).head)

  }

  //  override def  search(): String = {
  //    val query = getQueryBuilder()
  //    put("evaluateResults", entityDao.search(query))
  //    forward();
  //  }
  override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val statType = getInt("statType").getOrElse(null)
    val evaluateResult = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    populateConditions(evaluateResult)
    evaluateResult.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    evaluateResult.where("evaluateResult.lesson.semester=:semester", semester)
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
  //    query.where("evaluateResult.lesson.teachDepart in (:teachDeparts)", department)
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
  def updateTeacher(): String = {
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
  override def info(@param("id") id: String): String = {
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

    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val query = OqlBuilder.from(classOf[EvaluateResult], "evaluateResult")
    query.where("evaluateResult.lesson.semester.id = :semesterId", semesterId)
    val results = entityDao.search(query)
    try {
      results foreach { result =>
        //      for (EvaluateResult result : results) {
        //        // TODO kang 怎么确定学生有没有一门课的考试资格
        val builder = OqlBuilder.from(classOf[ExamTaker], "examTaker")
        builder.where("examTaker.lesson=:lesson", result.lesson)
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
