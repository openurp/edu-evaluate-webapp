package org.openurp.edu.evaluation.course.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Semester
import org.openurp.edu.base.model.Project
import org.openurp.edu.evaluation.lesson.model.TextEvaluation
import org.beangle.commons.collection.Order
import org.openurp.base.model.Department
import java.time.LocalDate

class TextEvaluationAction extends ProjectRestfulAction[TextEvaluation] {

  override protected def indexSetting(): Unit = {
    put("semesters", getSemesters())
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
    put("departments", findItemsBySchool(classOf[Department]))
  }
  override def search(): String = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val state = getBoolean("state").getOrElse(null)
    val textEvaluation = OqlBuilder.from(classOf[TextEvaluation], "textEvaluation")
    textEvaluation.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    if (state != null)
      textEvaluation.where("textEvaluation.state=:state", state)
    textEvaluation.where("textEvaluation.lesson.semester=:semester", semester)
    put("textEvaluations", entityDao.search(textEvaluation))
    forward()
  }

  /**
   * 修改(是否确认)
   *
   * @return
   */
  def updateAffirm(): View = {
    val semesterId = 20141
    val projectId = 1
    put("semester", entityDao.get(classOf[Semester], semesterId))
    put("project", entityDao.get(classOf[Project], projectId))
    val ids = longIds(simpleEntityName)
    val state = getBoolean("state").get

    val textEvaluations = entityDao.find(classOf[TextEvaluation], ids)
    textEvaluations foreach { textEvaluation =>
      textEvaluation.state = state
    }
    //    for (TextEvaluation textEvaluation : textEvaluations) {
    //      textEvaluation.setIsAffirm(isAffirm);
    //    }
    entityDao.saveOrUpdate(textEvaluations);
    redirect("search", "info.action.success")
  }
}
