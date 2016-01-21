package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.course.model.TextEvaluateSwitch
import org.openurp.edu.base.model.Project

class TextEvaluateSwitchAction extends RestfulAction[TextEvaluateSwitch] {
  
    protected override def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val projects =entityDao.getAll(classOf[Project])
    put("projects",projects)
    val defaultProject=OqlBuilder.from(classOf[Project],"project").where("project.id=:id",1)
    put("defaultProject",entityDao.search(defaultProject).head)
  }

  override def  search():String= {
    val opened =getBoolean("opened").getOrElse(true)
    val semesterId =getInt("semester.id").getOrElse(0)
    val textEvaluationSwitchs = OqlBuilder.from(classOf[TextEvaluateSwitch],"textEvaluateSwitch")
    if (semesterId!=0)
    textEvaluationSwitchs .where("textEvaluateSwitch.semester.id=:semesterId", semesterId)
    textEvaluationSwitchs .where("textEvaluateSwitch.opened=:opened", opened)
    put("textEvaluationSwitchs", entityDao.search(textEvaluationSwitchs))
    forward()
  }
  
  
  override def editSetting(entity: TextEvaluateSwitch) {
      put("semesters", entityDao.getAll(classOf[Semester]))
      put("projects",entityDao.getAll(classOf[Project]))
  }
  

 override def saveAndRedirect(evaluateSwitch: TextEvaluateSwitch): View = {    
    if (!evaluateSwitch.persisted) {
      val query = OqlBuilder.from(classOf[TextEvaluateSwitch], "textEvaluateSwitch");
      query.where("textEvaluateSwitch.semester.id =:semesterId", evaluateSwitch.semester.id);
      val textEvaluateSwitchs = entityDao.search(query);
      if (!textEvaluateSwitchs.isEmpty) {
        return redirect("search", "&textEvaluateSwitch.semester.id=" + evaluateSwitch.semester.id, "该学期评教开关已存在,请删除后再新增!");
      }
    }
    try {
      saveOrUpdate(evaluateSwitch);
       redirect("search", "success ,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id,"info.save.success")
    } catch {
      case e: Exception =>
        e.printStackTrace();
         redirect("search",  "failure,&evaluateSwitch.semester.id=" + evaluateSwitch.semester.id,"info.save.failure")
    }
 }

}