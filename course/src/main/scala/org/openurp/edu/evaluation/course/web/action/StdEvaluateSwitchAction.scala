package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson
import org.openurp.edu.lesson.model.Lesson
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.app.lesson.model.StdEvaluateSwitch
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.base.model.Project
import org.beangle.webmvc.api.annotation.action

class StdEvaluateSwitchAction extends ProjectRestfulAction[StdEvaluateSwitch] {

  protected override def indexSetting(): Unit = {
    put("semesters", getSemesters())
    //    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", new java.util.Date())
    //    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): String = {
    val opened = getBoolean("evaluateSwitch.opened")
    val semesterId = getInt("evaluateSwitch.semester.id")
    val queryQuestionnaire = OqlBuilder.from[Array[Any]](classOf[QuestionnaireLesson].getName, "questionnaireLesson")
    semesterId.foreach { semesterId => queryQuestionnaire.where("questionnaireLesson.lesson.semester.id =:semesterId", semesterId) }
    queryQuestionnaire.where("questionnaireLesson.lesson.project =:project", currentProject)
    queryQuestionnaire.groupBy("questionnaireLesson.lesson.semester.id ")
    queryQuestionnaire.select("questionnaireLesson.lesson.semester.id,count(*)")
    val countMap = entityDao.search(queryQuestionnaire).map(a => (a(0).asInstanceOf[Int], a(1).asInstanceOf[Number])).toMap
    put("countMap", countMap)
    val queryLesson = OqlBuilder.from[Array[Any]](classOf[Lesson].getName, "lesson")
    semesterId.foreach { semesterId => queryLesson.where("lesson.semester.id =:semesterId", semesterId) }
    queryLesson.where("lesson.project =:project", currentProject)
    // 排除(已有问卷)
    queryLesson.where("not exists(from " + classOf[QuestionnaireLesson].getName + " questionnaireLesson"
      + " where questionnaireLesson.lesson = lesson)")
    queryLesson.groupBy("lesson.semester.id")
    queryLesson.select("lesson.semester.id, count(*)")
    val lessonCountMap = entityDao.search(queryLesson).map(a => (a(0).asInstanceOf[Int], a(1).asInstanceOf[Number])).toMap
    put("lessonCountMap", lessonCountMap)
    val stdEvaluateSwitchs = getQueryBuilder()
    semesterId.foreach { semesterId => stdEvaluateSwitchs.where("stdEvaluateSwitch.semester.id=:semesterId", semesterId) }
    stdEvaluateSwitchs.where("stdEvaluateSwitch.project=:project", currentProject)
    opened.foreach { opened => stdEvaluateSwitchs.where("stdEvaluateSwitch.opened=:opened", opened) }
    put("stdEvaluateSwitchs", entityDao.search(stdEvaluateSwitchs))
    forward()
  }

  override def editSetting(entity: StdEvaluateSwitch): Unit = {
    //    put("semesterId", getLong("evaluateSwitch.semester.id").get)
    //    put("semesterId", 20141)
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("projects", entityDao.getAll(classOf[Project]))
  }

  override def saveAndRedirect(evaluateSwitch: StdEvaluateSwitch): View = {
    if (!evaluateSwitch.persisted) {
      val query = OqlBuilder.from(classOf[StdEvaluateSwitch], "evaluateSwitch")
      query.where("evaluateSwitch.semester.id =:semesterId", evaluateSwitch.semester.id)
      query.where("evaluateSwitch.project.id =:project", evaluateSwitch.project.id)
      val evaluateSwitchs = entityDao.search(query)
      if (!evaluateSwitchs.isEmpty) {
        return redirect("search", "该学期评教开关已存在,请删除后再新增!", "&evaluateSwitch.project.id=" + evaluateSwitch.project.id + "&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id)
      }
    }
    try {
      saveOrUpdate(evaluateSwitch)
      redirect("search", "info.save.success", "success,&evaluateSwitch.project.id=" + evaluateSwitch.project.id + "&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id)
    } catch {
      case e: Exception =>
        redirect("search", "info.save.failure", "failure,&evaluateSwitch.project.id=" + evaluateSwitch.project.id + "&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id)
    }
  }

}