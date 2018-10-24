/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.evaluation.department.web.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.evaluation.department.model.DepartEvaluate
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import org.beangle.commons.collection.Order
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
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
