package org.openurp.edu.evaluation.department.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import org.beangle.commons.collection.Order
import org.openurp.base.model.Department
import org.openurp.base.model.Semester
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class DepartEvaluateSearchAction extends ProjectRestfulAction[DepartEvaluate] {

  override def indexSetting(): Unit = {
    put("departments", findItemsBySchool(classOf[Department]))
    put("semesters", getSemesters())
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    put("currentSemester", entityDao.search(semesterQuery).head)
  }

  override def search(): View = {
    // 页面条件
    val semesterQuery = OqlBuilder.from(classOf[Semester], "semester").where(":now between semester.beginOn and semester.endOn", LocalDate.now)
    val semesterId = getInt("departEvaluate.semester.id").getOrElse(entityDao.search(semesterQuery).head.id)
    val semester = entityDao.get(classOf[Semester], semesterId)
    val departEvaluates = OqlBuilder.from(classOf[DepartEvaluate], "departEvaluate")
    populateConditions(departEvaluates)
    departEvaluates.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
    departEvaluates.where("departEvaluate.semester=:semester", semester)
    put("departEvaluates", entityDao.search(departEvaluates))
    forward()
  }
}
