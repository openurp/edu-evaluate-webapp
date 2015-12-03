package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.model.QuestionnaireLesson
import org.openurp.edu.lesson.model.Lesson
import org.beangle.webmvc.api.view.View
import org.openurp.edu.evaluation.course.model.StdEvaluateSwitch
import org.openurp.base.model.Semester

class StdEvaluateSwitchAction extends RestfulAction[StdEvaluateSwitch] {

  protected override def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    put("semester", semesters.head)
  }
  override def search(): String = {
//    val semesterId = getInt("evaluateSwitch.semester.id").get;
//    val projectId = getInt("evaluateSwitch.project.id")
    val semesterId =20141
    val projectId =1 
    //    semesterId = semesterId != null ? semesterId : getSemester().getId();
    val queryQuestionnaire = OqlBuilder.from(classOf[QuestionnaireLesson], "questionnaireLesson");
    queryQuestionnaire.where("questionnaireLesson.lesson.semester.id =:semesterId", semesterId);
//    projectId foreach { pid =>
      queryQuestionnaire.where("questionnaireLesson.lesson.project.id =:projectId", projectId);
//    }
    queryQuestionnaire.select("select count(*)");
    put("questionnaireCount", entityDao.search(queryQuestionnaire).head);
    val queryLesson = OqlBuilder.from(classOf[Lesson], "lesson");
    queryLesson.where("lesson.semester.id =:semesterId", semesterId);
//    projectId foreach { pid =>
      queryLesson.where("lesson.project.id =:projectId", projectId);
//    }
    // 排除(已有问卷)
    queryLesson.where("not exists(from " + classOf[QuestionnaireLesson].getName() + " questionnaireLesson"
      + " where questionnaireLesson.lesson = lesson)");
    queryLesson.select("select count(*)");
    put("lessonCount", entityDao.search(queryLesson).head);

    put(shortName + "s", entityDao.search(getQueryBuilder().where(shortName + ".semester.id=:semesterId", semesterId)));
    forward();
  }

  override def editSetting(entity: StdEvaluateSwitch) {
//    put("semesterId", getLong("evaluateSwitch.semester.id"));
    put("semesterId", 20141);
  }

  override def saveAndRedirect(entity: StdEvaluateSwitch): View = {
    val evaluateSwitch = entity.asInstanceOf[StdEvaluateSwitch]
    if (!evaluateSwitch.persisted) {
      val query = OqlBuilder.from(classOf[StdEvaluateSwitch], "evaluateSwitch");
      query.where("evaluateSwitch.semester.id =:semesterId", 20141);
      query.where("evaluateSwitch.project.id =:project", 1);
//      query.where("evaluateSwitch.semester.id =:semesterId", evaluateSwitch.semester.id);
//      query.where("evaluateSwitch.project.id =:project", evaluateSwitch.project.id);
      val evaluateSwitchs = entityDao.search(query);
      if (!evaluateSwitchs.isEmpty) {
        return redirect("search", "该学期评教开关已存在,请删除后再新增!",
          "&evaluateSwitch.project.id=" + 1 + "&evaluateSwitch.semester.id=" + 20141);
//            + evaluateSwitch.semester.id);
      }
//      evaluateSwitch.project.id = getInt("evaluateSwitch.project.id").get
//      evaluateSwitch.project.id = 1
    }
    //    evaluateSwitch.updatedAt=new java.util.Date()
    try {
      saveOrUpdate(evaluateSwitch);
       redirect("search", "info.save.success", "success,&evaluateSwitch.project.id=" + 1 + "&evaluateSwitch.semester.id=" + 20141)
    } catch {
      case e: Exception =>
         redirect("search", "info.save.failure", "failure,&evaluateSwitch.project.id=" + 1+ "&evaluateSwitch.semester.id=" + 20141)
    }
  }

}