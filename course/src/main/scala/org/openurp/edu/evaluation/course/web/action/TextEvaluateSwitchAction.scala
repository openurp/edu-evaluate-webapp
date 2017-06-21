package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.app.lesson.model.TextEvaluateSwitch
import org.openurp.edu.base.model.Project
import org.beangle.webmvc.api.annotation.action

class TextEvaluateSwitchAction extends ProjectRestfulAction[TextEvaluateSwitch] {

  protected override def indexSetting(): Unit = {
    put("semesters", getSemesters())
  }

  override def search(): String = {
    val opened = getBoolean("opened")
    val semesterId = getInt("semester.id")
    val textEvaluationSwitchs = OqlBuilder.from(classOf[TextEvaluateSwitch], "textEvaluateSwitch")
    semesterId.foreach { semesterId => textEvaluationSwitchs.where("textEvaluateSwitch.semester.id=:semesterId", semesterId) }
    opened.foreach { opened => textEvaluationSwitchs.where("textEvaluateSwitch.opened=:opened", opened) }
    textEvaluationSwitchs.where("textEvaluateSwitch.project=:project", currentProject)
    put("textEvaluationSwitchs", entityDao.search(textEvaluationSwitchs))
    forward()
  }

  override def editSetting(entity: TextEvaluateSwitch) {
    put("semesters", getSemesters())
  }

  override def saveAndRedirect(evaluateSwitch: TextEvaluateSwitch): View = {
    if (!evaluateSwitch.persisted) {
      val query = OqlBuilder.from(classOf[TextEvaluateSwitch], "textEvaluateSwitch")
      query.where("textEvaluateSwitch.semester.id =:semesterId", evaluateSwitch.semester.id)
      val textEvaluateSwitchs = entityDao.search(query)
      if (!textEvaluateSwitchs.isEmpty) {
        return redirect("search", "&textEvaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "该学期评教开关已存在,请删除后再新增!")
      }
    }
    try {
      saveOrUpdate(evaluateSwitch)
      redirect("search", "success ,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "info.save.success")
    } catch {
      case e: Exception =>
        e.printStackTrace()
        redirect("search", "failure,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "info.save.failure")
    }
  }

}