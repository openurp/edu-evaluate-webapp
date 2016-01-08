package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.base.model.Semester
import org.openurp.edu.evaluation.course.model.TextEvaluateSwitch

class TextEvaluateSwitchAction extends RestfulAction[TextEvaluateSwitch] {
  
    protected override def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    put("semester", semesters.head)
  }

// def getSwitch():TextEvaluateSwitch = {
//     val iterator:Iterator[TextEvaluateSwitch] = entityDao.getAll(classOf[TextEvaluateSwitch]).iterator
//    if (iterator.hasNext)
//      iterator.next() 
//    else new TextEvaluateSwitch()
//  }

  override def  search():String= {
    val semesterId =20141
    val projectId =1 
    val textEvaluationSwitchs = entityDao.search(OqlBuilder.from(classOf[TextEvaluateSwitch],"textEvaluateSwitch").where("textEvaluateSwitch.semester.id=:semesterId", semesterId))
    put("textEvaluationSwitchs", textEvaluationSwitchs)
    forward()
  }
  
  
  override def editSetting(entity: TextEvaluateSwitch) {
//    put("semesterId", getLong("evaluateSwitch.semester.id"));
    put("semesterId", 20141);
  }
  
//  override def  edit() :String={
//    put("textEvaluationSwitch", getSwitch());
//    return forward();
//  }

  override def  save():View = {
    val openedTeacher = getBoolean("textEvaluationSwitch.openedTeacher");
    val opened = getBoolean("textEvaluationSwitch.opened");
    val textEvaluateOpened = getBoolean("textEvaluationSwitch.textEvaluateOpened");
    val beginAt = getDateTime("textEvaluationSwitch.beginAt");
    val endAt = getDateTime("textEvaluationSwitch.endAt");
    val textSwitch = new TextEvaluateSwitch()
    textSwitch.openedTeacher=openedTeacher.get
    textSwitch.opened= opened.get
    textSwitch.textEvaluateOpened= textEvaluateOpened.get
    textSwitch.beginAt= beginAt.get
    textSwitch.endAt=endAt.get
    try {
      saveOrUpdate(textSwitch);
    } catch {
      case e: Exception =>
      redirect("index", "保存失败");
    }
     redirect("index", "info.save.success");
  }

}