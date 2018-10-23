package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.course.model.QuestionnaireClazz
import org.openurp.edu.course.model.Clazz
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.app.lesson.model.StdEvaluateSwitch
import org.openurp.edu.base.model.Semester
import org.openurp.edu.evaluation.model.Questionnaire
import org.openurp.edu.base.model.Project
import org.beangle.webmvc.api.annotation.action
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Project

class StdEvaluateSwitchAction extends ProjectRestfulAction[StdEvaluateSwitch] {

  protected override def indexSetting(): Unit = {
    put("semesters", getSemesters())
  }

  override def search(): View = {
    val opened = getBoolean("evaluateSwitch.opened")
    val semesterId = getInt("evaluateSwitch.semester.id")
    val queryQuestionnaire = OqlBuilder.from[Array[Any]](classOf[QuestionnaireClazz].getName, "questionnaireClazz")
    semesterId.foreach { semesterId => queryQuestionnaire.where("questionnaireClazz.lesson.semester.id =:semesterId", semesterId) }
    queryQuestionnaire.where("questionnaireClazz.lesson.project =:project", currentProject)
    queryQuestionnaire.groupBy("questionnaireClazz.lesson.semester.id ")
    queryQuestionnaire.select("questionnaireClazz.lesson.semester.id,count(*)")
    val countMap = entityDao.search(queryQuestionnaire).map(a => (a(0).asInstanceOf[Int], a(1).asInstanceOf[Number])).toMap
    put("countMap", countMap)
    val queryClazz = OqlBuilder.from[Array[Any]](classOf[Clazz].getName, "lesson")
    semesterId.foreach { semesterId => queryClazz.where("lesson.semester.id =:semesterId", semesterId) }
    queryClazz.where("lesson.project =:project", currentProject)
    // 排除(已有问卷)
    queryClazz.where("not exists(from " + classOf[QuestionnaireClazz].getName + " questionnaireClazz"
      + " where questionnaireClazz.lesson = lesson)")
    queryClazz.groupBy("lesson.semester.id")
    queryClazz.select("lesson.semester.id, count(*)")
    val lessonCountMap = entityDao.search(queryClazz).map(a => (a(0).asInstanceOf[Int], a(1).asInstanceOf[Number])).toMap
    put("lessonCountMap", lessonCountMap)
    val stdEvaluateSwitchs = getQueryBuilder()
    semesterId.foreach { semesterId => stdEvaluateSwitchs.where("stdEvaluateSwitch.semester.id=:semesterId", semesterId) }
    stdEvaluateSwitchs.where("stdEvaluateSwitch.project=:project", currentProject)
    opened.foreach { opened => stdEvaluateSwitchs.where("stdEvaluateSwitch.opened=:opened", opened) }
    put("stdEvaluateSwitchs", entityDao.search(stdEvaluateSwitchs))
    forward()
  }

  override def editSetting(entity: StdEvaluateSwitch): Unit = {
    val project = entityDao.findBy(classOf[Project], "code", List(get("project").get)).head;
    val query = OqlBuilder.from(classOf[Semester], "s").where("s.calendar in(:calendars)", project.calendars)
    put("semesters", entityDao.search(query))
    put("project", project);
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