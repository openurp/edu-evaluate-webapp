package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.lesson.model.TextEvaluation
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Order
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class TextEvaluationSearchAction extends RestfulAction[TextEvaluation] {

  override protected def indexSetting(): Unit = {
    val semesters = entityDao.getAll(classOf[Semester])
    put("semesters", semesters)
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val state = getBoolean("textEvaluation.state").getOrElse(null)
    val textEvaluation = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    textEvaluation.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    if (state != null)
      textEvaluation.where("textEvaluation.state=:state", state)
    textEvaluation.where("textEvaluation.lesson.semester=:semester", semester)
    put("textEvaluations", entityDao.search(textEvaluation))
    forward()
  }

}
